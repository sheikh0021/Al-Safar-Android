package com.application.al_safar.domain.repository

import com.application.al_safar.data.remote.dto.EmergencyContactDto

interface EmergencyRepository {
    suspend fun getEmergencyContacts(
        category: String? = null,
        location: String? = null
    ): Result<List<EmergencyContactDto>>
}
