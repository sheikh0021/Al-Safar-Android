package com.application.al_safar.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.data.local.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val isLoggedIn: Flow<Boolean> = preferencesManager.isLoggedIn

    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> = _userId

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    init {
        loadSession()
    }

    fun loadSession() {
        viewModelScope.launch {
            val id = preferencesManager.userId.first()?.toIntOrNull() ?: 0
            _userId.value = id

            val name = preferencesManager.userName.first()
            _userName.value = name
        }
    }
}
