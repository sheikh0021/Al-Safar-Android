package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.GroupDao
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.api.GroupApi
import com.application.al_safar.data.remote.dto.group.CreateGroupRequest
import com.application.al_safar.data.remote.dto.group.InviteMemberRequest
import com.application.al_safar.domain.model.Group
import com.application.al_safar.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GroupRepositoryImpl(
    private val groupApi: GroupApi,
    private val groupDao: GroupDao,
    private val preferencesManager: PreferencesManager
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> {
        return groupDao.getGroupsByUserId(getCurrentUserId()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getGroupById(groupId: String): Result<Group> = runCatching {
        val response = groupApi.getGroupById(groupId)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to fetch group: ${response.message()}")
        }
    }

    override suspend fun createGroup(name: String): Result<Group> = runCatching {
        val response = groupApi.createGroup(CreateGroupRequest(name))
        if (response.isSuccessful && response.body() != null) {
            val group = response.body()!!.toDomain()
            groupDao.insertGroup(group.toEntity())
            group
        } else {
            throw Exception("Failed to create group: ${response.message()}")
        }
    }

    override suspend fun inviteMember(groupId: String, email: String): Result<Unit> = runCatching {
        val response = groupApi.inviteMember(groupId, InviteMemberRequest(email))
        if (!response.isSuccessful) {
            throw Exception("Failed to invite member: ${response.message()}")
        }
    }

    override suspend fun removeMember(groupId: String, userId: String): Result<Unit> = runCatching {
        val response = groupApi.removeMember(groupId, userId)
        if (!response.isSuccessful) {
            throw Exception("Failed to remove member: ${response.message()}")
        }
    }

    override suspend fun syncGroups() {
        try {
            val response = groupApi.getGroups()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.forEach { dto ->
                    groupDao.insertGroup(dto.toDomain().toEntity())
                }
            }
        } catch (e: Exception) {
            // Use cached data on failure
        }
    }

    private fun getCurrentUserId(): String = runCatching {
        kotlinx.coroutines.runBlocking { preferencesManager.userId.first() ?: "" }
    }.getOrDefault("")
}
