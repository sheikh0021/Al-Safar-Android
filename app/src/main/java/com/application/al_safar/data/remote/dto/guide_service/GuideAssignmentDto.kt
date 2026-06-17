package com.application.al_safar.data.remote.dto.guide_service


data class GuideAssignmentDto(
    val id: Int,
    val guide: LocalGuideDto?,
    val traveler: TravelerDto?,
    val booking_id: Int?,
    val travel_date: String?,
    val package_title: String?,
    val status: String,
    val assigned_at: String,
    val unread_count: Int,
    val traveler_passport_url: String? = null,
    val traveler_photo_url: String? = null
)

data class LocalGuideDto(
    val id: Int,
    val user: GuideUserDto,
    val bio: String?,
    val city: String,
    val languages_list: List<String>,
    val is_available: Boolean,
    val rating: String,
    val profile_picture_url: String?
)

data class GuideUserDto(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val full_name: String,
    val phone_number: String?
)

data class TravelerDto(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val full_name: String,
    val phone_number: String?
)
