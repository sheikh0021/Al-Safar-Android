package com.application.al_safar.ui.guide


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.usecase.guide.GetMyGuideUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGuideViewModel @Inject constructor(
    private val getMyGuideUseCase: GetMyGuideUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<MyGuideUiState>(MyGuideUiState.Loading)
    val uiState: StateFlow<MyGuideUiState> = _uiState

    init {
        loadMyGuide()
    }
    fun loadMyGuide() {
        viewModelScope.launch {
            _uiState.value = MyGuideUiState.Loading
            getMyGuideUseCase()
                .onSuccess { assignments ->
                    _uiState.value = MyGuideUiState.Success(assignments)
                }
                .onFailure { error ->
                    _uiState.value = MyGuideUiState.Error(error.message ?: "Failed to load guide")
                }
        }
    }
}

sealed class MyGuideUiState{
    object Loading: MyGuideUiState()
    data class Success(val assignments: List<GuideAssignment>): MyGuideUiState()
    data class Error(val message: String): MyGuideUiState()
}