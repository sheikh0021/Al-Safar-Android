package com.application.al_safar.domain.usecase.booking

import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.repository.BookingRepository
import javax.inject.Inject

class ConfirmPaymentUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: Int, transactionId: String, paymentMethod: String): Result<Booking> {
        return repository.confirmPayment(bookingId, transactionId, paymentMethod)
    }
}
