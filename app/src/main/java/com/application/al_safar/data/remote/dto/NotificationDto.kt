package com.application.al_safar.data.remote.dto

import com.google.gson.annotations.SerializedName


data class NotificationDto(
    val id: Int,
    val title: String,
    val body: String,
    @SerializedName("notification_type")
    val notificationType: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    val data: Map<String, Any> = emptyMap(),
    @SerializedName("booking")
    val bookingId: Int? = null,
    @SerializedName("group")
    val groupId: Int? = null,
)

data class NotificationPreferenceDto(
    @SerializedName("notify_booking_confirmation")
    val notifyBookingConfirmation: Boolean = true,
    @SerializedName("notify_booking_cancelled")
    val notifyBookingCancelled: Boolean = true,
    @SerializedName("notify_payment_status")
    val notifyPaymentStatus: Boolean = true,
    @SerializedName("notify_guide_assigned")
    val notifyGuideAssigned: Boolean = true,
    @SerializedName("notify_guide_messages")
    val notifyGuideMessages: Boolean = true,
    @SerializedName("notify_trip_reminders")
    val notifyTripReminders: Boolean = true,
    @SerializedName("notify_group_announcements")
    val notifyGroupAnnouncements: Boolean = true,
    @SerializedName("quiet_hours_enabled")
    val quietHoursEnabled: Boolean = false,
    @SerializedName("quiet_hours_start")
    val quietHoursStart: String = "22:00",
    @SerializedName("quiet_hours_end")
    val quietHoursEnd: String = "08:00",
)

data class DeviceTokenRegisterRequest(
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("device_name")
    val deviceName: String,
    @SerializedName("device_type")
    val deviceType: String = "android",
)

data class UnreadCountResponse(
    @SerializedName("unread_count")
    val unreadCount: Int,
)
