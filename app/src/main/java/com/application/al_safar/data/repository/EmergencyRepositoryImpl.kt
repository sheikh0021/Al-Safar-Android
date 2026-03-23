package com.application.al_safar.data.repository

import com.application.al_safar.data.remote.api.EmergencyApi
import com.application.al_safar.data.remote.dto.EmergencyContactDto
import com.application.al_safar.domain.repository.EmergencyRepository

class EmergencyRepositoryImpl(
    private val emergencyApi: EmergencyApi
) : EmergencyRepository {

    override suspend fun getEmergencyContacts(
        category: String?,
        location: String?
    ): Result<List<EmergencyContactDto>> = runCatching {
        val response = emergencyApi.getEmergencyContacts(category, location)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception("Failed to fetch emergency contacts: ${response.message()}")
        }
    }
}
