package com.application.al_safar.data.remote.dto.trip


data class TripDto(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val flightNumber: String?,
    val hotelName: String?,
    val hotelAddress: String?,
    val notes: String?
)
