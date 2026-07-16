package com.application.al_safar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.application.al_safar.core.ApiConfig
import dagger.hilt.android.HiltAndroidApp
import org.maplibre.android.MapLibre
import javax.inject.Inject


@HiltAndroidApp
class AlSafarApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Shared Ktor/network code reads this; flavors still live in app BuildConfig.
        ApiConfig.initialize(BuildConfig.API_BASE_URL)
        MapLibre.getInstance(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val channels = listOf(
                NotificationChannel(
                    "booking_confirmed",
                    "Booking Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications about your bookings"
                },
                NotificationChannel(
                    "guide_assigned",
                    "Guide Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications when guides are assigned"
                },
                NotificationChannel(
                    "new_message",
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "New messages from your guide"
                },
                NotificationChannel(
                    "trip_reminder",
                    "Trip Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Reminders about upcoming trips"
                },
                NotificationChannel(
                    "emergency_alert",
                    "Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Emergency and safety alerts"
                },
                NotificationChannel(
                    "general",
                    "General",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "General notifications"
                }
            )
            
            notificationManager.createNotificationChannels(channels)
        }
    }
}
