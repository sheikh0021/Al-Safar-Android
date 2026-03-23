package com.application.al_safar.domain.model

import java.util.Date


data class Group(
    val id: String,
    val name: String,
    val leaderId: String,
    val memberIds: List<String>,
    val createdAt: Date
)
