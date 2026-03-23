package com.application.al_safar.data.remote.dto.group


data class GroupDto(
    val id: String,
    val name: String,
    val leaderId: String,
    val members: List<GroupMemberDto>,
    val createdAt: String
)

data class GroupMemberDto(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String
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
