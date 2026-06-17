package com.application.al_safar.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.application.al_safar.MainActivity
import com.application.al_safar.R
import com.application.al_safar.data.local.database.AlSafarDatabase
import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.remote.api.NotificationApi
import com.application.al_safar.data.remote.dto.DeviceTokenRegisterRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Handles incoming FCM messages and creates local notifications.
 * This service is called automatically by Firebase when a message arrives.
 */
@AndroidEntryPoint
class AlSafarMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var database: AlSafarDatabase

    @Inject
    lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var notificationApi: NotificationApi

    private val tag = "AlSafarMessagingService"

    /**
     * Called when message is received.
     * This handles both notification messages (with visual notification)
     * and data messages (silent background messages).
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(tag, "Message received from: ${remoteMessage.from}")

        // Handle notification message
        remoteMessage.notification?.let {
            handleNotificationMessage(
                title = it.title ?: "Al-Safar",
                body = it.body ?: "",
                data = remoteMessage.data
            )
        }

        // Handle data message (for background sync)
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage.data)
        }
    }

    /**
     * Called if FCM registration token is updated.
     * This happens when:
     * - App deletes instance ID
     * - App is restored on new device
     * - User uninstalls/reinstalls app
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(tag, "New FCM Token: $token")

        // Save the token to local storage
        CoroutineScope(Dispatchers.IO).launch {
            preferencesManager.saveFcmToken(token)
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken != null) {
                uploadFcmTokenToBackend(token)
            }
        }
    }

    /**
     * Handle notification message received from backend.
     * Creates a visible notification in the notification drawer.
     */
    private fun handleNotificationMessage(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        Log.d(tag, "Handling notification: $title")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Check if user has disabled this notification type
                val prefs = database.notificationDao().getPreferences().first()
                val notificationType = data["notification_type"] ?: "generic"

                if (!shouldShowNotification(notificationType, prefs)) {
                    Log.d(tag, "Notification type $notificationType is disabled")
                    return@launch
                }

                // Check quiet hours
                if (isInQuietHours(prefs)) {
                    Log.d(tag, "In quiet hours, scheduling for later")
                    // Could schedule using WorkManager here
                }

                // Save to local database
                val notification = NotificationEntity(
                    id = data["notification_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                    title = title,
                    body = body,
                    notificationType = notificationType,
                    isRead = false,
                    createdAt = System.currentTimeMillis().toString(),
                    data = Gson().toJson(data),
                )

                database.notificationDao().insertNotification(notification)

                // Show the local notification
                showLocalNotification(title, body, data)

            } catch (e: Exception) {
                Log.e(tag, "Error handling notification", e)
            }
        }
    }

    /**
     * Handle silent data messages.
     * Used for background sync without showing visual notification.
     */
    private fun handleDataMessage(data: Map<String, String>) {
        Log.d(tag, "Handling data message")

        val action = data["action"] ?: return

        when (action) {
            "sync_notifications" -> syncNotificationsFromBackend()
            "new_message" -> handleNewMessage(data)
            "booking_update" -> handleBookingUpdate(data)
            else -> Log.d(tag, "Unknown action: $action")
        }
    }

    /**
     * Show a local notification in the notification drawer.
     * Creates notification channel for Android 8.0+
     */
    private fun showLocalNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = data["notification_type"] ?: "general"

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getChannelName(channelId),
                getChannelImportance(channelId)
            ).apply {
                description = getChannelDescription(channelId)
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create pending intent for when notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification_data", Gson().toJson(data))
            data["screen"]?.let { putExtra("target_screen", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    /**
     * Determine if notification should be shown based on type and preferences.
     */
    private fun shouldShowNotification(
        notificationType: String,
        preferences: com.application.al_safar.data.local.entities.NotificationPreferenceEntity?
    ): Boolean {
        if (preferences == null) return true

        return when (notificationType) {
            "booking_confirmed" -> preferences.notifyBookingConfirmation
            "booking_cancelled" -> preferences.notifyBookingCancelled
            "payment_received" -> preferences.notifyPaymentStatus
            "guide_assigned" -> preferences.notifyGuideAssigned
            "new_message" -> preferences.notifyGuideMessages
            "trip_reminder" -> preferences.notifyTripReminders
            "group_announcement" -> preferences.notifyGroupAnnouncements
            "emergency_alert" -> preferences.notifyEmergencies
            else -> true
        }
    }

    /**
     * Check if current time falls within quiet hours.
     */
    private fun isInQuietHours(
        preferences: com.application.al_safar.data.local.entities.NotificationPreferenceEntity?
    ): Boolean {
        if (preferences == null || !preferences.quietHoursEnabled) return false

        val now = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(preferences.quietHoursStart, formatter)
        val end = LocalTime.parse(preferences.quietHoursEnd, formatter)

        return if (start.isBefore(end)) {
            // Quiet hours don't wrap around midnight
            now.isAfter(start) && now.isBefore(end)
        } else {
            // Quiet hours wrap around midnight
            now.isAfter(start) || now.isBefore(end)
        }
    }

    /**
     * Get notification channel name based on type.
     */
    private fun getChannelName(channelId: String): String = when (channelId) {
        "booking_confirmed" -> "Booking Updates"
        "guide_assigned" -> "Guide Updates"
        "new_message" -> "Messages"
        "trip_reminder" -> "Trip Reminders"
        "emergency_alert" -> "Emergency Alerts"
        else -> "General"
    }

    /**
     * Get notification channel importance based on type.
     */
    private fun getChannelImportance(channelId: String): Int = when (channelId) {
        "emergency_alert" -> NotificationManager.IMPORTANCE_MAX
        "new_message" -> NotificationManager.IMPORTANCE_HIGH
        else -> NotificationManager.IMPORTANCE_DEFAULT
    }

    /**
     * Get notification channel description.
     */
    private fun getChannelDescription(channelId: String): String = when (channelId) {
        "booking_confirmed" -> "Notifications about your bookings"
        "guide_assigned" -> "Notifications when guides are assigned"
        "new_message" -> "New messages from your guide"
        "trip_reminder" -> "Reminders about upcoming trips"
        "emergency_alert" -> "Emergency and safety alerts"
        else -> "General notifications"
    }

    /**
     * Handle new message notification.
     * Could update chat UI or sync in background.
     */
    private fun handleNewMessage(data: Map<String, String>) {
        // Implementation for syncing new messages
        Log.d(tag, "New message: ${data["content"]}")
    }

    /**
     * Handle booking update notification.
     */
    private fun handleBookingUpdate(data: Map<String, String>) {
        Log.d(tag, "Booking update: ${data["status"]}")
    }

    /**
     * Sync notifications from backend.
     */
    private fun syncNotificationsFromBackend() {
        Log.d(tag, "Syncing notifications from backend")
        // Implementation would call API to fetch recent notifications
    }

    /**
     * Upload FCM token to backend server.
     * This ensures backend can send notifications to this device.
     */
    private suspend fun uploadFcmTokenToBackend(token: String) {
        try {
            val response = notificationApi.registerDeviceToken(
                DeviceTokenRegisterRequest(
                    fcmToken = token,
                    deviceName = Build.MODEL,
                    deviceType = "android"
                )
            )
            if (response.isSuccessful) {
                Log.d(tag, "FCM token uploaded to backend")
            } else {
                Log.w(tag, "FCM token registration failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error uploading FCM token", e)
        }
    }
}