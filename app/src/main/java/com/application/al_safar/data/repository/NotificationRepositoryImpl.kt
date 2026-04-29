package com.application.al_safar.data.repository


import android.util.Log
import com.application.al_safar.data.local.dao.NotificationDao
import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.remote.api.NotificationApi
import com.application.al_safar.data.remote.dto.DeviceTokenRegisterRequest
import com.application.al_safar.data.remote.dto.NotificationDto
import com.application.al_safar.data.remote.dto.NotificationPreferenceDto
import com.application.al_safar.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao,
    private val api: NotificationApi,
    private val preferencesManager: PreferencesManager,
) : NotificationRepository {

    override fun getRecentNotifications(limit: Int): Flow<List<NotificationEntity>> {
        return dao.getRecentNotifications(limit)
    }

    override fun getUnreadNotifications(): Flow<List<NotificationEntity>> {
        return dao.getUnreadNotifications()
    }

    override fun getUnreadCount(): Flow<Int> {
        return dao.getUnreadCount()
    }

    override fun getNotificationById(id: Int): Flow<NotificationEntity?> {
        return dao.getNotificationById(id)
    }

    override suspend fun markAsRead(id: Int) {
        try {
            // Mark locally first
            dao.markAsRead(id)

            // Sync with backend
            api.markAsRead(id)
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error marking as read", e)
            // Still mark locally even if backend fails
            dao.markAsRead(id)
        }
    }

    override suspend fun markAllAsRead() {
        try {
            dao.markAllAsRead()
            api.markAllAsRead()
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error marking all as read", e)
            dao.markAllAsRead()
        }
    }

    override suspend fun saveNotification(notification: NotificationEntity) {
        dao.insertNotification(notification)
    }

    override suspend fun saveNotifications(notifications: List<NotificationEntity>) {
        dao.insertNotifications(notifications)
    }

    override fun getPreferences(): Flow<NotificationPreferenceEntity?> {
        return dao.getPreferences()
    }

    override suspend fun savePreferences(preferences: NotificationPreferenceEntity) {
        dao.insertPreferences(preferences)
    }

    override suspend fun registerFcmToken(fcmToken: String): Result<Unit> {
        return try {
            val request = DeviceTokenRegisterRequest(
                fcmToken = fcmToken,
                deviceName = android.os.Build.MODEL,
                deviceType = "android"
            )

            val response = api.registerDeviceToken(request)

            if (response.isSuccessful) {
                preferencesManager.saveFcmToken(fcmToken)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to register FCM token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error registering FCM token", e)
            Result.failure(e)
        }
    }

    override suspend fun syncNotificationsFromBackend(): Result<List<NotificationDto>> {
        return try {
            val response = api.getRecentNotifications(limit = 50)

            if (response.isSuccessful) {
                val body = response.body()
                if (body is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    val notifications = (body["results"] as? List<NotificationDto>) ?: emptyList()
                    Result.success(notifications)
                } else {
                    Result.success(emptyList())
                }
            } else {
                Result.failure(Exception("Failed to sync notifications: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error syncing from backend", e)
            Result.failure(e)
        }
    }

    override suspend fun markAsReadOnBackend(notificationId: Int): Result<Unit> {
        return try {
            val response = api.markAsRead(notificationId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark as read: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error marking as read on backend", e)
            Result.failure(e)
        }
    }

    override suspend fun getPreferencesFromBackend(): Result<NotificationPreferenceDto> {
        return try {
            val response = api.getPreferences()

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to fetch preferences: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error fetching preferences", e)
            Result.failure(e)
        }
    }

    override suspend fun updatePreferencesOnBackend(preferences: NotificationPreferenceDto): Result<Unit> {
        return try {
            val response = api.updatePreferences(preferences)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update preferences: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error updating preferences", e)
            Result.failure(e)
        }
    }
}