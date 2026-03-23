package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "duas")
data class DuaEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val arabicText: String,
    val transliteration: String?,
    val translation: String?,
    val category: String,
    val audioUrl: String?,
    val isFavorite: Boolean = false,
    val lastSyncedAt: Long?
)
