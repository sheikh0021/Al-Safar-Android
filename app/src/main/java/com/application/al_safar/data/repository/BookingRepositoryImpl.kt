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
import okhttp3.RequestBody.Companion.toRequestBody
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
        val cleanDocumentType = documentType.trim().lowercase()

        val allowedTypes = setOf("passport", "visa", "photo", "vaccine", "other")
        require(cleanDocumentType in allowedTypes) {
            "Invalid document type: $documentType"
        }

        val documentTypePart = cleanDocumentType
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val mediaType = when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "pdf" -> "application/pdf"
            else -> "application/octet-stream"
        }.toMediaTypeOrNull()

        val requestFile = file.asRequestBody(mediaType)

        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestFile
        )

        val response = bookingApi.uploadDocument(
            bookingId = bookingId,
            documentType = documentTypePart,
            file = filePart
        )

        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Failed to upload document: ${errorBody ?: response.message()} ")
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
