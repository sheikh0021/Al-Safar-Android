package com.application.al_safar.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Dua
import com.application.al_safar.domain.model.UmrahStep
import com.application.al_safar.domain.usecase.guide.GetDuasUseCase
import com.application.al_safar.domain.usecase.guide.GetUmrahStepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UmrahGuideViewModel @Inject constructor(
    private val getUmrahStepsUseCase: GetUmrahStepsUseCase,
    private val getDuasUseCase: GetDuasUseCase
) : ViewModel() {

    private val _stepsState = MutableStateFlow<UmrahGuideUiState>(UmrahGuideUiState.Loading)
    val stepsState: StateFlow<UmrahGuideUiState> = _stepsState.asStateFlow()

    private val _duasState = MutableStateFlow<DuasUiState>(DuasUiState.Loading)
    val duasState: StateFlow<DuasUiState> = _duasState.asStateFlow()

    init {
        loadUmrahSteps()
    }

    fun loadUmrahSteps() {
        viewModelScope.launch {
            getUmrahStepsUseCase().collect { steps ->
                _stepsState.value = UmrahGuideUiState.Success(steps)
            }
        }
    }

    fun loadDuas(category: String? = null) {
        viewModelScope.launch {
            getDuasUseCase(category).collect { duas ->
                _duasState.value = DuasUiState.Success(duas)
            }
        }
    }
}

sealed class UmrahGuideUiState {
    object Loading : UmrahGuideUiState()
    data class Success(val steps: List<UmrahStep>) : UmrahGuideUiState()
    data class Error(val message: String) : UmrahGuideUiState()
}

sealed class DuasUiState {
    object Loading : DuasUiState()
    data class Success(val duas: List<Dua>) : DuasUiState()
    data class Error(val message: String) : DuasUiState()
}
