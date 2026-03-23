package com.application.al_safar.domain.usecase.booking

import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(): Flow<List<Booking>> {
        return repository.getBookings()
    }
}
