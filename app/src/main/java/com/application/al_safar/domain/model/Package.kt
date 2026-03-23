package com.application.al_safar.domain.model


data class Package(
    val id: Int,
    val title: String,
    val description: String,
    val packageType: String,
    val durationDays: Int,
    val price: String,
    val imageUrl: String?,
    val includes: String,
    val hotelName: String?,
    val hotelRating: Int?,
    val distanceFromHaram: String?,
    val isActive: Boolean
)
