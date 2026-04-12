package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.model.GuideMessage


interface GuideServiceRepository {
    suspend fun getMyGuide(): Result<List<GuideAssignment>>
    suspend fun getTravelerMessages(assignmentId: Int): Result<List<GuideMessage>>
    suspend fun sendTravelerMessage(assignmentId: Int, content: String): Result<GuideMessage>
    suspend fun getMyTravelers(): Result<List<GuideAssignment>>
    suspend fun getGuideMessages(assignmentId: Int): Result<List<GuideMessage>>
    suspend fun sendGuideMessage(assignmentId: Int, content: String): Result<GuideMessage>
}