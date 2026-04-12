package com.application.al_safar.domain.usecase.guide

import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.repository.GuideServiceRepository
import javax.inject.Inject


class GetMyGuideUseCase @Inject constructor(
    private val repository: GuideServiceRepository
) {
    suspend operator fun invoke(): Result<List<GuideAssignment>> {
        return repository.getMyGuide()
    }
}