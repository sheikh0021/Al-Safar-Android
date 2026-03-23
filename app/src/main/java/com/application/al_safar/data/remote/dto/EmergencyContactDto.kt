package com.application.al_safar.data.remote.dto


data class EmergencyContactDto(
    val id: String,
    val name: String,
    val arabicName: String?,
    val phoneNumber: String,
    val category: String,
    val description: String?
)
