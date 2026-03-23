package com.application.al_safar.data.remote.dto.booking


data class BookingDocumentDto(
    val id: Int,
    val document_type: String,
    val file_url: String,
    val file_name: String,
    val uploaded_at: String
)
