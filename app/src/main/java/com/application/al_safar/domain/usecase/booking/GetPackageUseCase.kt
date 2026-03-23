package com.application.al_safar.domain.usecase.booking

import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.repository.BookingRepository
import javax.inject.Inject


class GetPackageUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(packageId: Int): Result<Package> {
        return repository.getPackage(packageId)
    }
}