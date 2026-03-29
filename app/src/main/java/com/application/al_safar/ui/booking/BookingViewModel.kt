package com.application.al_safar.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.model.Group
import com.application.al_safar.domain.repository.BookingRepository
import com.application.al_safar.domain.repository.GroupRepository
import com.application.al_safar.domain.usecase.booking.ConfirmPaymentUseCase
import com.application.al_safar.domain.usecase.booking.CreateBookingUseCase
import com.application.al_safar.domain.usecase.booking.UploadDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val createBookingUseCase: CreateBookingUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val groupRepository: GroupRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val bookingState: StateFlow<BookingUiState> = _bookingState.asStateFlow()

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    private val _createdBookingId = MutableStateFlow<Int?>(null)
    val createdBookingId: StateFlow<Int?> = _createdBookingId.asStateFlow()

    private val _currentBooking = MutableStateFlow<Booking?>(null)
    val currentBooking: StateFlow<Booking?> = _currentBooking.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun loadGroups() {
        viewModelScope.launch {
            groupRepository.getGroups().collect { groupList ->
                _groups.value = groupList
            }
        }
    }

    fun createBooking(
        packageId: Int,
        travelDate: LocalDate,
        returnDate: LocalDate,
        numberOfTravelers: Int,
        groupId: Int?
    ) {
        viewModelScope.launch {
            _bookingState.value = BookingUiState.Loading
            createBookingUseCase(
                packageId = packageId,
                travelDate = travelDate.format(formatter),
                returnDate = returnDate.format(formatter),
                numberOfTravelers = numberOfTravelers,
                groupId = groupId
            ).onSuccess { booking ->
                _createdBookingId.value = booking.id
                _currentBooking.value = booking
                _bookingState.value = BookingUiState.BookingCreated(booking.id)
            }.onFailure { error ->
                _bookingState.value =
                    BookingUiState.Error(error.message ?: "Failed to create booking")
            }
        }
    }

    fun uploadDocument(bookingId: Int, documentType: String, file: File) {
        viewModelScope.launch {
            _bookingState.value = BookingUiState.Loading
            uploadDocumentUseCase(bookingId, documentType, file)
                .onSuccess { _bookingState.value = BookingUiState.DocumentUploaded }
                .onFailure { error ->
                    _bookingState.value =
                        BookingUiState.Error(error.message ?: "Failed to upload document")
                }
        }
    }

    fun confirmPayment(bookingId: Int, transactionId: String) {
        viewModelScope.launch {
            _bookingState.value = BookingUiState.Loading
            confirmPaymentUseCase(bookingId, transactionId)
                .onSuccess { booking ->
                    _currentBooking.value = booking
                    _bookingState.value = BookingUiState.PaymentConfirmed
                }.onFailure { error ->
                    _bookingState.value =
                        BookingUiState.Error(error.message ?: "Payment failed")
                }
        }
    }

    fun loadBooking(bookingId: Int) {
        viewModelScope.launch {
            bookingRepository.getBooking(bookingId)
                .onSuccess { booking ->
                    _currentBooking.value = booking
                    _bookingState.value = BookingUiState.Idle
                }.onFailure { error ->
                    _bookingState.value =
                        BookingUiState.Error(error.message ?: "Failed to load booking")
                }
        }
    }

    fun resetState() {
        _bookingState.value = BookingUiState.Idle
    }
}

sealed class BookingUiState {
    object Idle : BookingUiState()
    object Loading : BookingUiState()
    data class BookingCreated(val bookingId: Int) : BookingUiState()
    object DocumentUploaded : BookingUiState()
    object PaymentConfirmed : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}
