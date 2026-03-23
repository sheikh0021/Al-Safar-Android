package com.application.al_safar.domain.model


data class Booking(
    val id: Int,
    val umrahPackage: Package,
    val travelDate: String,
    val returnDate: String,
    val numberOfTravelers: Int,
    val group: Group?,
    val totalAmount: String,
    val status: String,
    val paymentStatus: String,
    val paymentTransactionId: String?,
    val documentsUploaded: Boolean,
    val documents: List<BookingDocument>,
    val createdAt: String,
    val updatedAt: String
)
