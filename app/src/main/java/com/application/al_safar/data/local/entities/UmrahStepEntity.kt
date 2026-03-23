package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "umrah_steps")
data class UmrahStepEntity(
    @PrimaryKey
    val id: String,
    val stepNumber: Int,
    val title: String,
    val description: String,
    val arabicText: String?,
    val isCompleted: Boolean = false,
    val category: String,
    val estimatedTime: Int?,
    val imageUrl: String?,
    val audioUrl: String?,
    val lastSyncedAt: Long?
)
