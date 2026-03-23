package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.booking.BookingDocumentDto
import com.application.al_safar.data.remote.dto.booking.BookingDto
import com.application.al_safar.data.remote.dto.booking.ConfirmPaymentRequest
import com.application.al_safar.data.remote.dto.booking.CreateBookingRequest
import com.application.al_safar.data.remote.dto.booking.PackageDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface BookingApi {
    @GET("booking/packages/")
    suspend fun getPackages(@Query("type") type: String? = null): Response<List<PackageDto>>

    @GET("booking/packages/{id}/")
    suspend fun getPackage(@Path("id") id: Int): Response<PackageDto>

    @GET("booking/bookings/")
    suspend fun getBookings(): Response<List<BookingDto>>

    @GET("booking/bookings/{id}/")
    suspend fun getBooking(@Path("id") id: Int): Response<BookingDto>

    @POST("booking/bookings/")
    suspend fun createBooking(@Body request: CreateBookingRequest): Response<BookingDto>

    @Multipart
    @POST("booking/bookings/{id}/upload_document/")
    suspend fun uploadDocument(
        @Path("id") bookingId: Int,
        @Part("document_type") documentType: String,
        @Part file: MultipartBody.Part
    ): Response<BookingDocumentDto>

    @POST("booking/bookings/{id}/confirm_payment/")
    suspend fun confirmPayment(
        @Path("id") bookingId: Int,
        @Body request: ConfirmPaymentRequest
    ): Response<BookingDto>

}