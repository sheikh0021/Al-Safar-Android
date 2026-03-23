package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.model.BookingDocument
import com.application.al_safar.domain.model.Package
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BookingRepository {
    suspend fun getPackages(type: String?): Result<List<Package>>
    suspend fun getPackage(packageId: Int): Result<Package>
    fun getBookings(): Flow<List<Booking>>
    suspend fun getBooking(bookingId: Int): Result<Booking>
    suspend fun createBooking(
        packageId: Int,
        travelDate: String,
        returnDate: String,
        numberOfTravelers: Int,
        groupId: Int?
    ): Result<Booking>
    suspend fun uploadDocument(
        bookingId: Int,
        documentType: String,
        file: File
    ): Result<BookingDocument>
    suspend fun confirmPayment(bookingId: Int, transactionId: String): Result<Booking>
}
