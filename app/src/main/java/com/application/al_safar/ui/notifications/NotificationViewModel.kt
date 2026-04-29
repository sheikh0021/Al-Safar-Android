package com.application.al_safar.ui.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.data.local.entities.NotificationEntity
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity
import com.application.al_safar.domain.usecase.notification.GetNotificationsUseCase
import com.application.al_safar.domain.usecase.notification.GetPreferencesUseCase
import com.application.al_safar.domain.usecase.notification.GetUnreadCountUseCase
import com.application.al_safar.domain.usecase.notification.MarkAsReadUseCase
import com.application.al_safar.domain.usecase.notification.SyncNotificationsUseCase
import com.application.al_safar.domain.usecase.notification.UpdatePreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUnreadCountUseCase: GetUnreadCountUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val syncNotificationsUseCase: SyncNotificationsUseCase,
    private val getPreferencesUseCase: GetPreferencesUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()

    private val _preferences = MutableStateFlow<NotificationPreferenceEntity?>(null)
    val preferences: StateFlow<NotificationPreferenceEntity?> = _preferences.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadNotifications()
        loadUnreadCount()
        loadPreferences()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                getNotificationsUseCase().collectLatest { notifications ->
                    _notifications.value = notifications
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error loading notifications", e)
                _error.value = e.message
            }
        }
    }

    private fun loadUnreadCount() {
        viewModelScope.launch {
            try {
                getUnreadCountUseCase().collectLatest { count ->
                    _unreadCount.value = count
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error loading unread count", e)
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                getPreferencesUseCase().collectLatest { prefs ->
                    _preferences.value = prefs
                }
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error loading preferences", e)
            }
        }
    }

    fun updatePreferences(preferences: NotificationPreferenceEntity) {
        viewModelScope.launch {
            try {
                updatePreferencesUseCase(preferences)
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error updating preferences", e)
                _error.value = e.message
            }
        }
    }

    fun syncNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = syncNotificationsUseCase()
            result.fold(
                onSuccess = {
                    Log.d("NotificationViewModel", "Notifications synced successfully")
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Unknown error"
                    _isLoading.value = false
                }
            )
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                markAsReadUseCase(notificationId)
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error marking as read", e)
                _error.value = e.message
            }
        }
    }
}