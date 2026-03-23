package com.application.al_safar.data.repository

import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.remote.api.BookingApi
import com.application.al_safar.data.remote.dto.booking.ConfirmPaymentRequest
import com.application.al_safar.data.remote.dto.booking.CreateBookingRequest
import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.model.BookingDocument
import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BookingRepositoryImpl(
    private val bookingApi: BookingApi
) : BookingRepository {

    override suspend fun getPackages(type: String?): Result<List<Package>> = runCatching {
        val response = bookingApi.getPackages(type)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.map { it.toDomain() }
        } else {
            throw Exception("Failed to fetch packages: ${response.message()}")
        }
    }

    override suspend fun getPackage(packageId: Int): Result<Package> = runCatching {
        val response = bookingApi.getPackage(packageId)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to fetch package: ${response.message()}")
        }
    }

    override fun getBookings(): Flow<List<Booking>> = flow {
        val response = bookingApi.getBookings()
        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.map { it.toDomain() })
        } else {
            emit(emptyList())
        }
    }

    override suspend fun getBooking(bookingId: Int): Result<Booking> = runCatching {
        val response = bookingApi.getBooking(bookingId)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to fetch booking: ${response.message()}")
        }
    }

    override suspend fun createBooking(
        packageId: Int,
        travelDate: String,
        returnDate: String,
        numberOfTravelers: Int,
        groupId: Int?
    ): Result<Booking> = runCatching {
        val request = CreateBookingRequest(
            package_id = packageId,
            travel_date = travelDate,
            return_date = returnDate,
            number_of_travelers = numberOfTravelers,
            group_id = groupId
        )
        val response = bookingApi.createBooking(request)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to create booking: ${response.message()}")
        }
    }

    override suspend fun uploadDocument(
        bookingId: Int,
        documentType: String,
        file: File
    ): Result<BookingDocument> = runCatching {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val response = bookingApi.uploadDocument(bookingId, documentType, filePart)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to upload document: ${response.message()}")
        }
    }

    override suspend fun confirmPayment(bookingId: Int, transactionId: String): Result<Booking> =
        runCatching {
            val response = bookingApi.confirmPayment(bookingId, ConfirmPaymentRequest(transactionId))
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.toDomain()
            } else {
                throw Exception("Payment confirmation failed: ${response.message()}")
            }
        }
}
