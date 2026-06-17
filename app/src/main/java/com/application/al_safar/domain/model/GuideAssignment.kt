package com.application.al_safar.domain.model

data class GuideAssignment(
    val id: Int,
    val guide: LocalGuide?,
    val travelerId: Int?,
    val travelerName: String?,
    val travelerEmail: String?,
    val bookingId: Int?,
    val travelDate: String?,
    val packageTitle: String?,
    val status: String,
    val assignedAt: String,
    val unreadCount: Int,
    val travelerPassportUrl: String? = null,
    val travelerPhotoUrl: String? = null

)
