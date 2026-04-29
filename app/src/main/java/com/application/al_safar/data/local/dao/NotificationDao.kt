package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.DeviceTokenEntity
import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentNotifications(limit: Int = 50): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = :id")
    fun getNotificationById(id: Int): Flow<NotificationEntity?>

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE createdAt < :cutoffTime")
    suspend fun deleteOldNotifications(cutoffTime: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: NotificationPreferenceEntity)

    @Query("SELECT * FROM notification_preferences LIMIT 1")
    fun getPreferences(): Flow<NotificationPreferenceEntity?>

    @Update
    suspend fun updatePreferences(preferences: NotificationPreferenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceToken(token: DeviceTokenEntity)

    @Query("SELECT * FROM device_tokens WHERE isActive = 1")
    suspend fun getActiveDeviceTokens(): List<DeviceTokenEntity>

    @Query("SELECT fcmToken FROM device_tokens WHERE isActive = 1 LIMIT 1")
    suspend fun getLatestFcmToken(): String?

    @Query("UPDATE device_tokens SET isActive = 0 WHERE id = :id")
    suspend fun deactivateToken(id: Int)


}