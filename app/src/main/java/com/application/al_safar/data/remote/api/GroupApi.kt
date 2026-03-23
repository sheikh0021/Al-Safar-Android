package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.group.AnnouncementDto
import com.application.al_safar.data.remote.dto.group.CreateGroupRequest
import com.application.al_safar.data.remote.dto.group.GroupDto
import com.application.al_safar.data.remote.dto.group.InviteMemberRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface GroupApi {
    @GET("groups/")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("groups/{id}/")
    suspend fun getGroupById(@Path("id") id: String): Response<GroupDto>

    @POST("groups/")
    suspend fun createGroup(@Body group: CreateGroupRequest): Response<GroupDto>

    @POST("groups/{id}/invite/")
    suspend fun inviteMember(@Path("id") groupId: String, @Body request: InviteMemberRequest): Response<Unit>

    @DELETE("groups/{id}/members/{userId}/")
    suspend fun removeMember(@Path("id") groupId: String, @Path("userId") userId: String): Response<Unit>

    @POST("groups/{id}/announcements/")
    suspend fun createAnnouncement(@Path("id") groupId: String, @Body announcement: AnnouncementDto): Response<AnnouncementDto>
}