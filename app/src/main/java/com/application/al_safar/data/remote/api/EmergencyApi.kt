package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.EmergencyContactDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface EmergencyApi {
    @GET("emergency/contacts/")
    suspend fun getEmergencyContacts(
        @Query("category") category: String? = null,
        @Query("location") location: String? = null
    ): Response<List<EmergencyContactDto>>
}