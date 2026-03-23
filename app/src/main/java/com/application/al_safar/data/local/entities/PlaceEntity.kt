package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val nameArabic: String?,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val description: String?,
    val imageUrl: String?,
    val phoneNumber: String?,
    val isFavorite: Boolean = false,
    val lastSyncedAt: Long?
)
