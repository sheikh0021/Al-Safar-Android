package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.TripEntity
import com.application.al_safar.data.remote.dto.trip.TripDto
import com.application.al_safar.domain.model.Trip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun TripDto.toDomain(userId: String = ""): Trip = Trip(
    id = id ?: UUID.randomUUID().toString(),
    userId = userId,
    title = title,
    startDate = runCatching { isoFormat.parse(startDate) ?: Date() }.getOrDefault(Date()),
    endDate = runCatching { isoFormat.parse(endDate) ?: Date() }.getOrDefault(Date()),
    flightNumber = flightNumber,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    notes = notes,
    isActive = false
)

fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    userId = userId,
    title = title,
    startDate = Date(startDate),
    endDate = Date(endDate),
    flightNumber = flightNumber,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    notes = notes,
    isActive = isActive
)

fun Trip.toEntity(): TripEntity = TripEntity(
    id = id,
    userId = userId,
    title = title,
    startDate = startDate.time,
    endDate = endDate.time,
    flightNumber = flightNumber,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    notes = notes,
    isActive = isActive,
    createdAt = System.currentTimeMillis(),
    lastSyncedAt = System.currentTimeMillis()
)

fun Trip.toDto(): TripDto = TripDto(
    id = id,
    title = title,
    startDate = isoFormat.format(startDate),
    endDate = isoFormat.format(endDate),
    flightNumber = flightNumber,
    hotelName = hotelName,
    hotelAddress = hotelAddress,
    notes = notes
)
