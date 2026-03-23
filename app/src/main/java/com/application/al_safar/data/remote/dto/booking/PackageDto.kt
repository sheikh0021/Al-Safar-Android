package com.application.al_safar.data.remote.dto.booking


data class PackageDto(
    val id: Int,
    val title: String,
    val description: String,
    val package_type: String,
    val duration_days: Int,
    val price: String,
    val image_url: String?,
    val includes: String,
    val hotel_name: String?,
    val hotel_rating: Int?,
    val distance_from_haram: String?,
    val is_active: Boolean
)
