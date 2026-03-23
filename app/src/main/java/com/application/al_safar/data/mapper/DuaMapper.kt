package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.DuaEntity
import com.application.al_safar.data.remote.dto.guide.DuaDto
import com.application.al_safar.domain.model.Dua

fun DuaDto.toDomain(): Dua = Dua(
    id = id,
    title = title,
    arabicText = arabicText,
    transliteration = transliteration,
    translation = translation,
    category = category,
    audioUrl = audioUrl,
    isFavorite = false
)

fun DuaEntity.toDomain(): Dua = Dua(
    id = id,
    title = title,
    arabicText = arabicText,
    transliteration = transliteration,
    translation = translation,
    category = category,
    audioUrl = audioUrl,
    isFavorite = isFavorite
)

fun Dua.toEntity(): DuaEntity = DuaEntity(
    id = id,
    title = title,
    arabicText = arabicText,
    transliteration = transliteration,
    translation = translation,
    category = category,
    audioUrl = audioUrl,
    isFavorite = isFavorite,
    lastSyncedAt = System.currentTimeMillis()
)
