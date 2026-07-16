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
            val access = preferencesManager.getAccessToken()
            val refresh = preferencesManager.refreshToken.first()
            val loggedIn = preferencesManager.isLoggedIn()
            _userRole.value = preferencesManager.userRole.first()

            // Need both tokens + sticky note. Missing refresh = cannot recover later.
            val looksValid = loggedIn && !access.isNullOrBlank() && !refresh.isNullOrBlank()

            if (!looksValid && loggedIn) {
                // Clean half-broken leftovers so next launch is clean
                preferencesManager.clearAll()
            }

            _isAuthenticated.value = looksValid
        }
    }
}
