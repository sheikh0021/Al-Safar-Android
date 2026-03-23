package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.map.PlaceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MapApi {
    @GET("places/")
    suspend fun getPlaces(@Query("category") category: String? = null): Response<List<PlaceDto>>

    @GET("places/{id}/")
    suspend fun getPlaceById(@Path("id") id: String): Response<PlaceDto>

    @GET("places/nearby/")
    suspend fun getNearbyPlaces(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 5000
    ): Response<List<PlaceDto>>
}