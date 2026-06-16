package com.application.al_safar.data.remote.dto.booking


data class ConfirmPaymentRequest(
    val transaction_id: String,
    val payment_method: String
)
