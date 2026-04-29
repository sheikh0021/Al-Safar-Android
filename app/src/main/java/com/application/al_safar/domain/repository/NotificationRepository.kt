package com.application.al_safar.domain.repository


import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity
import com.application.al_safar.data.remote.dto.NotificationDto
import com.application.al_safar.data.remote.dto.NotificationPreferenceDto
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    // Local operations
    fun getRecentNotifications(limit: Int = 50): Flow<List<NotificationEntity>>
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    fun getUnreadCount(): Flow<Int>
    fun getNotificationById(id: Int): Flow<NotificationEntity?>

    suspend fun markAsRead(id: Int)
    suspend fun markAllAsRead()

    suspend fun saveNotification(notification: NotificationEntity)
    suspend fun saveNotifications(notifications: List<NotificationEntity>)

    // Preferences (Local)
    fun getPreferences(): Flow<NotificationPreferenceEntity?>
    suspend fun savePreferences(preferences: NotificationPreferenceEntity)

    // Remote operations
    suspend fun registerFcmToken(fcmToken: String): Result<Unit>
    suspend fun syncNotificationsFromBackend(): Result<List<NotificationDto>>
    suspend fun markAsReadOnBackend(notificationId: Int): Result<Unit>
    suspend fun getPreferencesFromBackend(): Result<NotificationPreferenceDto>
    suspend fun updatePreferencesOnBackend(preferences: NotificationPreferenceDto): Result<Unit>
}