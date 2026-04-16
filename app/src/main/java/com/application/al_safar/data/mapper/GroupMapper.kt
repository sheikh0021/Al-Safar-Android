package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.GroupEntity
import com.application.al_safar.data.remote.dto.group.GroupDto
import com.application.al_safar.domain.model.Group
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
private val gson = Gson()

fun GroupDto.toDomain(): Group = Group(
    id = id.toString(),
    name = name,
    leaderId = leader_id?.toString() ?: "",
    memberIds = members.map { it.id.toString() },
    createdAt = runCatching { isoFormat.parse(created_at) ?: Date() }.getOrDefault(Date())
)

fun GroupEntity.toDomain(): Group {
    val memberIds: List<String> = runCatching {
        val type = object : TypeToken<List<String>>() {}.type
        gson.fromJson<List<String>>(this.memberIds, type)
    }.getOrDefault(emptyList())
    return Group(
        id = id,
        name = name,
        leaderId = leaderId,
        memberIds = memberIds,
        createdAt = Date(createdAt)
    )
}

fun Group.toEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    leaderId = leaderId,
    memberIds = gson.toJson(memberIds),
    createdAt = createdAt.time,
    lastSyncedAt = System.currentTimeMillis()
)
