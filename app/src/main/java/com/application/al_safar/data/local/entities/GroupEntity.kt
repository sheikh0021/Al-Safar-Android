package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val leaderId: String,
    val memberIds: String,
    val createdAt: Long,
    val lastSyncedAt: Long?
)
