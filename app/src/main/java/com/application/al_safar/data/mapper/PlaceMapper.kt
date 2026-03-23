package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.PlaceEntity
import com.application.al_safar.data.remote.dto.map.PlaceDto
import com.application.al_safar.domain.model.Place

fun PlaceDto.toDomain(): Place = Place(
    id = id,
    name = name,
    nameArabic = nameArabic,
    category = category,
    latitude = latitude,
    longitude = longitude,
    address = address,
    description = description,
    imageUrl = imageUrl,
    phoneNumber = phoneNumber,
    isFavorite = false
)

fun PlaceEntity.toDomain(): Place = Place(
    id = id,
    name = name,
    nameArabic = nameArabic,
    category = category,
    latitude = latitude,
    longitude = longitude,
    address = address,
    description = description,
    imageUrl = imageUrl,
    phoneNumber = phoneNumber,
    isFavorite = isFavorite
)

fun Place.toEntity(): PlaceEntity = PlaceEntity(
    id = id,
    name = name,
    nameArabic = nameArabic,
    category = category,
    latitude = latitude,
    longitude = longitude,
    address = address,
    description = description,
    imageUrl = imageUrl,
    phoneNumber = phoneNumber,
    isFavorite = isFavorite,
    lastSyncedAt = System.currentTimeMillis()
)
