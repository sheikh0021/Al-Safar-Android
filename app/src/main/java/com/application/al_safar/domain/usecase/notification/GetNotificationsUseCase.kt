package com.application.al_safar.domain.usecase.notification


import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity
import com.application.al_safar.data.remote.dto.NotificationPreferenceDto
import com.application.al_safar.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(limit: Int = 50): Flow<List<NotificationEntity>> {
        return repository.getRecentNotifications(limit)
    }
}

class GetUnreadCountUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getUnreadCount()
    }
}

class MarkAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int) {
        repository.markAsRead(notificationId)
    }
}

class GetPreferencesUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<NotificationPreferenceEntity?> {
        return repository.getPreferences()
    }
}

class UpdatePreferencesUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(preferences: NotificationPreferenceEntity) {
        // Update locally
        repository.savePreferences(preferences)
        
        // Update on backend
        repository.updatePreferencesOnBackend(
            NotificationPreferenceDto(
                notifyBookingConfirmation = preferences.notifyBookingConfirmation,
                notifyBookingCancelled = preferences.notifyBookingCancelled,
                notifyPaymentStatus = preferences.notifyPaymentStatus,
                notifyGuideAssigned = preferences.notifyGuideAssigned,
                notifyGuideMessages = preferences.notifyGuideMessages,
                notifyTripReminders = preferences.notifyTripReminders,
                notifyGroupAnnouncements = preferences.notifyGroupAnnouncements,
                quietHoursEnabled = preferences.quietHoursEnabled,
                quietHoursStart = preferences.quietHoursStart,
                quietHoursEnd = preferences.quietHoursEnd
            )
        )
    }
}

class SyncNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val result = repository.syncNotificationsFromBackend()
            result.fold(
                onSuccess = { notifications ->
                    repository.saveNotifications(
                        notifications.map {
                            it.toEntity()
                        }
                    )
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Extension function to convert DTO to Entity
private fun com.application.al_safar.data.remote.dto.NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        title = this.title,
        body = this.body,
        notificationType = this.notificationType,
        isRead = this.isRead,
        createdAt = this.createdAt,
        data = com.google.gson.Gson().toJson(this.data),
        bookingId = this.bookingId,
        groupId = this.groupId,
    )
}