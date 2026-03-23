package com.application.al_safar.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.data.local.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    init {
        viewModelScope.launch {
            _selectedLanguage.value = preferencesManager.getLanguage() ?: "en"
        }
    }

    fun selectLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun saveLanguage() {
        viewModelScope.launch {
            preferencesManager.saveLanguage(_selectedLanguage.value)
        }
    }
}
