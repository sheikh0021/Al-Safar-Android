package com.application.al_safar.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.repository.AuthRepository
import com.application.al_safar.domain.usecase.guide.AcceptAssignmentUseCase
import com.application.al_safar.domain.usecase.guide.GetMyTravelersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GuideDashboardUiState {
    object Loading : GuideDashboardUiState()
    data class Success(val assignments: List<GuideAssignment>) : GuideDashboardUiState()
    data class Error(val message: String) : GuideDashboardUiState()
}

sealed class AcceptAssignmentState{
    object Idle : AcceptAssignmentState()
    object Loading : AcceptAssignmentState()
    data class Success(val assignment : GuideAssignment) : AcceptAssignmentState()
    data class Error(val message: String) : AcceptAssignmentState()
}

sealed class GuideTab {
    object Trips   : GuideTab()
    object Chats   : GuideTab()
    object Profile : GuideTab()
}

@HiltViewModel
class GuideDashboardViewModel @Inject constructor(
    private val getMyTravelersUseCase: GetMyTravelersUseCase,
    private val acceptAssignmentUseCase: AcceptAssignmentUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GuideDashboardUiState>(GuideDashboardUiState.Loading)
    val uiState: StateFlow<GuideDashboardUiState> = _uiState

    private val _selectedTab = MutableStateFlow<GuideTab>(GuideTab.Trips)
    val selectedTab: StateFlow<GuideTab> = _selectedTab.asStateFlow()

    private val _acceptState = MutableStateFlow<AcceptAssignmentState>(AcceptAssignmentState.Idle)
    val acceptState: StateFlow<AcceptAssignmentState> = _acceptState.asStateFlow()

    private val _logoutComplete = MutableStateFlow(false)
    val logoutComplete: StateFlow<Boolean> = _logoutComplete

    init {
        loadTravelers()
    }

    fun loadTravelers() {
        viewModelScope.launch {
            _uiState.value = GuideDashboardUiState.Loading
            getMyTravelersUseCase()
                .onSuccess { assignments ->
                    _uiState.value = GuideDashboardUiState.Success(assignments)
                }
                .onFailure { error ->
                    _uiState.value = GuideDashboardUiState.Error(
                        error.message ?: "Failed to load assignments"
                    )
                }
        }
    }

    fun selectTab(tab: GuideTab) {
        _selectedTab.value = tab
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutComplete.value = true
        }
    }

    fun getAssignments(): List<GuideAssignment> =
        (_uiState.value as? GuideDashboardUiState.Success)?.assignments ?: emptyList()

    fun getActiveTrips(): List<GuideAssignment> =
        getAssignments().filter { it.status.lowercase() == "active" }

    fun getOngoingTrips(): List<GuideAssignment> =
        getAssignments().filter { it.status.lowercase() == "ongoing" }

    fun getCompletedTrips(): List<GuideAssignment> =
        getAssignments().filter { it.status.lowercase() == "completed" }

    fun getCancelledTrips(): List<GuideAssignment> =
        getAssignments().filter { it.status.lowercase() == "cancelled" }

    fun acceptAssignment(assignmentId: Int) {
        viewModelScope.launch {
            _acceptState.value = AcceptAssignmentState.Loading
            acceptAssignmentUseCase(assignmentId)
                .onSuccess { updatedAssignment ->
                    _acceptState.value = AcceptAssignmentState.Success(updatedAssignment)
                    loadTravelers()
                }
                .onFailure { error ->
                    _acceptState.value = AcceptAssignmentState.Error(
                        error.message ?: "Failed to accept assignment"
                    )
                }
        }
    }

    fun getPendingTrips(): List<GuideAssignment> = getAssignments().filter {
        it.status.lowercase() == "pending_acceptance"
    }

    fun resetAcceptState() {
        _acceptState.value = AcceptAssignmentState.Idle
    }

    fun getTotalUnreadCount(): Int =
        getAssignments().sumOf { it.unreadCount }
}