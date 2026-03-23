package com.application.al_safar.domain.model


data class BookingDocument(
    val id: Int,
    val documentType: String,
    val fileUrl: String,
    val fileName: String,
    val updatedAt: String
)
