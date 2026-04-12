package com.application.al_safar.domain.usecase.guide

import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.domain.repository.GuideServiceRepository
import javax.inject.Inject


class SendMessageUseCase @Inject constructor(
    private val repository: GuideServiceRepository
) {
    suspend operator fun invoke(
        assignmentId: Int,
        content: String,
        isGuide: Boolean
    ): Result<GuideMessage> {
        return if (isGuide)
            repository.sendGuideMessage(assignmentId, content)
        else
            repository.sendTravelerMessage(assignmentId, content)
    }
}