package com.application.al_safar.domain.usecase.trip

import com.application.al_safar.domain.model.Trip
import com.application.al_safar.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTripsUseCase @Inject constructor(private val repository: TripRepository) {
    operator fun invoke(): Flow<List<Trip>> {
        return repository.getTrips()
    }
}
