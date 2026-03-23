package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.UmrahStepEntity
import com.application.al_safar.data.remote.dto.guide.UmrahStepDto
import com.application.al_safar.domain.model.UmrahStep


fun UmrahStepDto.toDomain(): UmrahStep {
    return UmrahStep(
        id = id,
        stepNumber = stepNumber,
        title = title,
        description = description,
        arabicText = arabicText,
        isCompleted = false,
        category = category,
        estimatedTime = estimatedTime,
        imageUrl = imageUrl,
        audioUrl = audioUrl

    )
}

fun UmrahStepEntity.toDomain(): UmrahStep {
    return UmrahStep(
        id = id,
        stepNumber = stepNumber,
        title = title,
        description = description,
        arabicText = arabicText,
        isCompleted = isCompleted,
        category = category,
        estimatedTime = estimatedTime,
        imageUrl = imageUrl,
        audioUrl = audioUrl
    )
}

fun UmrahStep.toEntity(): UmrahStepEntity {
    return UmrahStepEntity(
        id = id,
        stepNumber = stepNumber,
        title = title,
        description = description,
        arabicText = arabicText,
        isCompleted = isCompleted,
        category = category,
        estimatedTime = estimatedTime,
        imageUrl = imageUrl,
        audioUrl = audioUrl,
        lastSyncedAt = System.currentTimeMillis()
    )
}