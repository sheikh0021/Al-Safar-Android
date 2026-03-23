package com.application.al_safar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val flightNumber: String?,
    val hotelName: String?,
    val hotelAddress: String?,
    val notes: String?,
    val isActive: Boolean = false,
    val createdAt: Long,
    val lastSyncedAt: Long?
)
