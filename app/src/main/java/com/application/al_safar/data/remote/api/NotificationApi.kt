package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.DeviceTokenRegisterRequest
import com.application.al_safar.data.remote.dto.NotificationDto
import com.application.al_safar.data.remote.dto.NotificationPreferenceDto
import com.application.al_safar.data.remote.dto.UnreadCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface NotificationApi {

    // Device Token Management
    @POST("notifications/device-tokens/")
    suspend fun registerDeviceToken(
        @Body request: DeviceTokenRegisterRequest
    ): Response<Map<String, Any>>

    @POST("notifications/device-tokens/cleanup-old-tokens/")
    suspend fun cleanupOldTokens(): Response<Map<String, Any>>

    // Notification Management
    @GET("notifications/")
    suspend fun getNotifications(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
    ): Response<Map<String, Any>>

    @GET("notifications/recent/")
    suspend fun getRecentNotifications(
        @Query("limit") limit: Int = 20,
    ): Response<Map<String, Any>>

    @GET("notifications/{id}/")
    suspend fun getNotificationDetail(
        @Path("id") notificationId: Int
    ): Response<NotificationDto>

    @POST("notifications/{id}/mark-as-read/")
    suspend fun markAsRead(
        @Path("id") notificationId: Int
    ): Response<NotificationDto>

    @POST("notifications/mark-all-as-read/")
    suspend fun markAllAsRead(): Response<Map<String, Any>>

    @GET("notifications/unread-count/")
    suspend fun getUnreadCount(): Response<UnreadCountResponse>

    // Preferences
    @GET("notifications/preferences/")
    suspend fun getPreferences(): Response<NotificationPreferenceDto>

    @PUT("notifications/preferences/")
    suspend fun updatePreferences(
        @Body preferences: NotificationPreferenceDto
    ): Response<NotificationPreferenceDto>
}