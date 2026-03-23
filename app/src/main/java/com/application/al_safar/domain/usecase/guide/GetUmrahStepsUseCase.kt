package com.application.al_safar.domain.usecase.guide

import com.application.al_safar.domain.model.UmrahStep
import com.application.al_safar.domain.repository.GuideRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUmrahStepsUseCase @Inject constructor(private val repository: GuideRepository) {
    operator fun invoke(): Flow<List<UmrahStep>> {
        return repository.getUmrahSteps()
    }
}