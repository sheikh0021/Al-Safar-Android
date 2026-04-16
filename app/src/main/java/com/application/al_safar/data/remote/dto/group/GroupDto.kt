package com.application.al_safar.data.remote.dto.group


data class GroupDto(
    val id: Int,
    val name: String,
    val leader_id: Int?,
    val members: List<GroupMemberDto>,
    val created_at: String
)

data class GroupMemberDto(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String
)

data class CreateGroupRequest(
    val name: String
)

data class InviteMemberRequest(
    val email: String
)

data class AnnouncementDto(
    val id: String?,
    val title: String,
    val message: String,
    val createdAt: String?
)
