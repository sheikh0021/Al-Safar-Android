package com.application.al_safar.domain.model

sealed class PaymentMethod(val apiValue: String){
    data object Cash : PaymentMethod("cash")

    data class Upi(
        val upiId: String
    ) : PaymentMethod("upi")

    data class Card(
        val cardHolderName: String,
        val cardNumber: String
    ) : PaymentMethod("card") {
        val lastFourDigits: String
            get() = cardNumber.takeLast(4)
    }
}
