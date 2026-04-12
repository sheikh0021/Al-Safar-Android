package com.application.al_safar.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.data.local.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    private val _userRole = MutableStateFlow("traveler")
    val userRole: StateFlow<String> = _userRole

    fun checkAuthStatus() {
        viewModelScope.launch {
            val token = preferencesManager.getAccessToken()
            val loggedIn = preferencesManager.isLoggedIn()
            _userRole.value = preferencesManager.userRole.first()
            _isAuthenticated.value = token != null && loggedIn
        }
    }
}
