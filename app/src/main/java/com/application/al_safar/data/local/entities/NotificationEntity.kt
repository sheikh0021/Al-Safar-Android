package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String,
    val notificationType: String,
    val isRead: Boolean = false,
    val createdAt: String,

    @SerializedName("data")
    val data: String,

    val bookingId: Int? = null,
    val groupId: Int? = null,

    val syncedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notification_preferences")
data class NotificationPreferenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val notifyBookingConfirmation: Boolean = true,
    val notifyBookingCancelled: Boolean = true,
    val notifyPaymentStatus: Boolean = true,
    val notifyGuideAssigned: Boolean = true,
    val notifyGuideMessages: Boolean = true,
    val notifyTripReminders: Boolean = true,
    val notifyGroupAnnouncements: Boolean = true,
    val notifyEmergencies: Boolean = true,
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: String = "22:00",
    val quietHoursEnd: String = "08:00",
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "device_tokens")
data class DeviceTokenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val fcmToken: String,
    val deviceName: String,
    val deviceType: String = "android",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsed: Long = System.currentTimeMillis()
)