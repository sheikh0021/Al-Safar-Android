package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.trip.ReminderDto
import com.application.al_safar.data.remote.dto.trip.TripDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface TripApi {
    @GET("trips/")
    suspend fun getTrips(): Response<List<TripDto>>

    @GET("trips/{id}/")
    suspend fun getTripById(@Path("id") id: String): Response<TripDto>

    @POST("trips/")
    suspend fun createTrip(@Body trip: TripDto): Response<TripDto>

    @PUT("trips/{id}/")
    suspend fun updateTrip(@Path("id") id: String, @Body trip: TripDto): Response<TripDto>

    @DELETE("trips/{id}/")
    suspend fun deleteTrip(@Path("id") id: String): Response<Unit>

    @GET("trips/{id}/reminders/")
    suspend fun getReminders(@Path("id") tripId: String): Response<List<ReminderDto>>

    @POST("trips/{id}/reminders/")
    suspend fun createReminder(@Path("id") tripId: String, @Body reminder: ReminderDto): Response<ReminderDto>
}