package com.application.al_safar.domain.usecase.booking


import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.repository.BookingRepository
import javax.inject.Inject

class GetPackagesUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(type: String?): Result<List<Package>> {
        return repository.getPackages(type)
    }
}