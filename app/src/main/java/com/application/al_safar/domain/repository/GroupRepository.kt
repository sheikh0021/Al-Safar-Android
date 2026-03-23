package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    suspend fun createGroup(name: String): Result<Group>
    suspend fun inviteMember(groupId: String, email: String): Result<Unit>
    suspend fun removeMember(groupId: String, userId: String): Result<Unit>
    suspend fun syncGroups()
}
