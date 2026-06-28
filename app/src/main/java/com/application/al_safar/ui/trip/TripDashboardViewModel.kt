package com.application.al_safar.ui.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.usecase.booking.GetBookingsUseCase
import com.application.al_safar.domain.usecase.guide.GetMyGuideUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDashboardViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val getMyGuideUseCase: GetMyGuideUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TripDashboardUiState>(TripDashboardUiState.Loading)
    val uiState: StateFlow<TripDashboardUiState> = _uiState

    private val _totalUnreadMessages = MutableStateFlow(0)
    val totalUnreadMessages: StateFlow<Int> = _totalUnreadMessages

    init {
        loadBookings()
        loadGuideAssignments()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = TripDashboardUiState.Loading
            getBookingsUseCase().collect { bookings ->
                _uiState.value = TripDashboardUiState.Success(bookings)
            }
        }
    }

    fun loadGuideAssignments() {
        viewModelScope.launch {
            getMyGuideUseCase()
                .onSuccess { assignments ->
                    _totalUnreadMessages.value = assignments.sumOf { it.unreadCount }
                }
                .onFailure {
                    _totalUnreadMessages.value = 0
                }
        }
    }

    fun refreshUnreadCount() {
        loadGuideAssignments()
    }
}

sealed class TripDashboardUiState {
    object Loading : TripDashboardUiState()
    data class Success(val bookings: List<Booking>) : TripDashboardUiState()
    data class Error(val message: String) : TripDashboardUiState()
}
