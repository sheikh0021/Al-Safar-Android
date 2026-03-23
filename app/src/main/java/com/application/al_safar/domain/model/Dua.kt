package com.application.al_safar.domain.model


data class Dua(
    val id: String,
    val title: String,
    val arabicText: String,
    val transliteration: String?,
    val translation: String?,
    val category: String,
    val audioUrl: String?,
    val isFavorite: Boolean
)
