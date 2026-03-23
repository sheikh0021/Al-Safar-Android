package com.application.al_safar.domain.model


data class Place(
    val id: String,
    val name: String,
    val nameArabic: String?,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val description: String?,
    val imageUrl: String?,
    val phoneNumber: String?,
    val isFavorite: Boolean
)
