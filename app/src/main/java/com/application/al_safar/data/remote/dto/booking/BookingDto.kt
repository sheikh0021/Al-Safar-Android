package com.application.al_safar.data.remote.dto.booking

import com.application.al_safar.data.remote.dto.group.GroupDto


data class BookingDto(
    val id: Int,
    val `package`: PackageDto,
    val travel_date: String,
    val return_date: String,
    val number_of_travelers: Int,
    val group: GroupDto?,
    val total_amount: String,
    val status: String,
    val payment_status: String,
    val payment_transaction_id: String?,
    val documents_uploaded: Boolean,
    val documents: List<BookingDocumentDto>,
    val created_at: String,
    val updated_at: String



)
