package com.application.al_safar.domain.usecase.guide

import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.domain.repository.GuideServiceRepository
import javax.inject.Inject


class GetMessagesUseCase @Inject constructor(
    private val repository: GuideServiceRepository
) {
    suspend operator fun invoke(
        assignmentId: Int,
        isGuide: Boolean
    ): Result<List<GuideMessage>> {
        return if (isGuide)
            repository.getGuideMessages(assignmentId)
        else
            repository.getTravelerMessages(assignmentId)
    }
}