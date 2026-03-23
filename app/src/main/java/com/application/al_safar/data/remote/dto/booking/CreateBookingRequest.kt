package com.application.al_safar.data.remote.dto.booking


data class CreateBookingRequest(
    val package_id: Int,
    val travel_date: String,
    val return_date: String,
    val number_of_travelers: Int,
    val group_id: Int? = null
)
