package com.application.al_safar.domain.usecase.booking

import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        packageId: Int,
        travelDate: String,
        returnDate: String,
        numberOfTravelers: Int,
        groupId: Int?
    ): Result<Booking> {
        return repository.createBooking(packageId, travelDate, returnDate, numberOfTravelers, groupId)
    }
}
