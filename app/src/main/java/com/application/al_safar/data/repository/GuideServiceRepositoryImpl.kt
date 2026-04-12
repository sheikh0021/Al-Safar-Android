package com.application.al_safar.data.repository

import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.remote.api.GuideServiceApi
import com.application.al_safar.data.remote.dto.guide_service.SendMessageRequest
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.domain.repository.GuideServiceRepository


class GuideServiceRepositoryImpl(
    private val guideServiceApi: GuideServiceApi
): GuideServiceRepository {
    override suspend fun getMyGuide(): Result<List<GuideAssignment>> = runCatching {
     val response = guideServiceApi.getMyGuide()
        if (response.isSuccessful && response.body() != null)
            response.body()!!.map { it.toDomain() }
        else
            throw Exception("Failed to load guide: ${response.message()}")
    }

    override suspend fun getTravelerMessages(assignmentId: Int): Result<List<GuideMessage>> = runCatching {
        val response = guideServiceApi.getTravelerMessages(assignmentId)
        if (response.isSuccessful && response.body() != null)
            response.body()!!.map { it.toDomain() }
        else
            throw Exception("Failed to load messages: ${response.message()}")
    }

    override suspend fun sendTravelerMessage(
        assignmentId: Int,
        content: String
    ): Result<GuideMessage> = runCatching {
  val response = guideServiceApi.sendTravelerMessage(assignmentId, SendMessageRequest(content))
  if (response.isSuccessful && response.body() != null)
      response.body()!!.toDomain()
        else
            throw Exception("Failed to send message: ${response.message()}")
    }

    override suspend fun getMyTravelers(): Result<List<GuideAssignment>> = runCatching {
 val response = guideServiceApi.getMyTravelers()
 if (response.isSuccessful && response.body() != null)
     response.body()!!.map { it.toDomain() }
        else
            throw Exception("Failed to load travelers: ${response.message()}")
    }

    override suspend fun getGuideMessages(assignmentId: Int): Result<List<GuideMessage>> = runCatching {
val response = guideServiceApi.getGuideMessages(assignmentId)
        if (response.isSuccessful && response.body() != null)
            response.body()!!.map { it.toDomain() }
        else
            throw Exception("Failed to load messages: ${response.message()}")
    }

    override suspend fun sendGuideMessage(
        assignmentId: Int,
        content: String
    ): Result<GuideMessage> = runCatching {
val response = guideServiceApi.sendGuideMessage(assignmentId, SendMessageRequest(content))
if (response.isSuccessful && response.body() != null)
    response.body()!!.toDomain()
        else
            throw Exception("Failed to send message: ${response.message()}")
    }
}