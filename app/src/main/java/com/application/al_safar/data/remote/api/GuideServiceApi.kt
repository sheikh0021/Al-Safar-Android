package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.guide_service.GuideAssignmentDto
import com.application.al_safar.data.remote.dto.guide_service.GuideMessageDto
import com.application.al_safar.data.remote.dto.guide_service.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface GuideServiceApi {
    @GET("guide-service/my-guide/")
    suspend fun getMyGuide(): Response<List<GuideAssignmentDto>>

    @GET("guide-service/chat/{assignmentId}/messages/")
    suspend fun getTravelerMessages(
        @Path("assignmentId") assignmentId: Int
    ): Response<List<GuideMessageDto>>

    @POST("guide-service/chat/{assignmentId}/messages/")
    suspend fun sendTravelerMessage(
        @Path("assignmentId") assignmentId: Int,
        @Body request: SendMessageRequest
    ): Response<GuideMessageDto>

    @GET("guide-service/my-travelers/")
    suspend fun getMyTravelers(): Response<List<GuideAssignmentDto>>

    @GET("guide-service/guide-chat/{assignmentId}/messages/")
    suspend fun getGuideMessages(
        @Path("assignmentId") assignmentId: Int
    ): Response<List<GuideMessageDto>>

    @POST("guide-service/guide-chat/{assignmentId}/messages/")
    suspend fun sendGuideMessage(
        @Path("assignmentId") assignmentId: Int,
        @Body request: SendMessageRequest
    ): Response<GuideMessageDto>
}