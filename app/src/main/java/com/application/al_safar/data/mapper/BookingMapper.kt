package com.application.al_safar.data.mapper

import com.application.al_safar.data.remote.dto.booking.BookingDocumentDto
import com.application.al_safar.data.remote.dto.booking.BookingDto
import com.application.al_safar.data.remote.dto.booking.PackageDto
import com.application.al_safar.domain.model.Booking
import com.application.al_safar.domain.model.BookingDocument
import com.application.al_safar.domain.model.Package

fun PackageDto.toDomain(): Package = Package(
    id = id,
    title = title,
    description = description,
    packageType = package_type,
    durationDays = duration_days,
    price = price,
    imageUrl = image_url,
    includes = includes,
    hotelName = hotel_name,
    hotelRating = hotel_rating,
    distanceFromHaram = distance_from_haram,
    isActive = is_active
)

fun BookingDocumentDto.toDomain(): BookingDocument = BookingDocument(
    id = id,
    documentType = document_type,
    fileUrl = file_url,
    fileName = file_name,
    updatedAt = uploaded_at
)

fun BookingDto.toDomain(): Booking = Booking(
    id = id,
    umrahPackage = `package`.toDomain(),
    travelDate = travel_date,
    returnDate = return_date,
    numberOfTravelers = number_of_travelers,
    group = group?.toDomain(),
    totalAmount = total_amount,
    status = status,
    paymentStatus = payment_status,
    paymentTransactionId = payment_transaction_id,
    documentsUploaded = documents_uploaded,
    documents = documents.map { it.toDomain() },
    createdAt = created_at,
    updatedAt = updated_at
)
