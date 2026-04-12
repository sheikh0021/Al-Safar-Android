package com.application.al_safar.data.mapper

import com.application.al_safar.data.remote.dto.guide_service.GuideAssignmentDto
import com.application.al_safar.data.remote.dto.guide_service.GuideMessageDto
import com.application.al_safar.data.remote.dto.guide_service.LocalGuideDto
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.domain.model.LocalGuide


fun LocalGuideDto.toDomain(): LocalGuide = LocalGuide(
    id = id,
    userId = user.id,
    firstName = user.first_name,
    lastName = user.last_name,
    email = user.email,
    phoneNumber = user.phone_number,
    bio = bio,
    city = city,
    languagesList = languages_list,
    isAvailable = is_available,
    rating = rating,
    profilePictureUrl = profile_picture_url
)

fun GuideAssignmentDto.toDomain(): GuideAssignment = GuideAssignment(
    id = id,
    guide = guide?.toDomain(),
    travelerId = traveler?.id,
    travelerName = traveler?.full_name,
    travelerEmail = traveler?.email,
    bookingId = booking_id,
    travelDate = travel_date,
    packageTitle = package_title,
    status = status,
    assignedAt = assigned_at,
    unreadCount = unread_count
)

fun GuideMessageDto.toDomain(): GuideMessage = GuideMessage(
    id = id,
    senderId = sender_id,
    senderName = sender_name,
    content = content,
    sentAt = sent_at,
    isRead = is_read
)