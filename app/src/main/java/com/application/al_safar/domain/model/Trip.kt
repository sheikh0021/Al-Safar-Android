package com.application.al_safar.domain.model

import java.util.Date


data class Trip(
    val id: String,
    val userId: String,
    val title: String,
    val startDate: Date,
    val endDate: Date,
    val flightNumber: String?,
    val hotelName: String?,
    val hotelAddress: String?,
    val notes: String?,
    val isActive: Boolean
)
