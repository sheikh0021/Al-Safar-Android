package com.application.al_safar.domain.usecase.booking

import com.application.al_safar.domain.model.BookingDocument
import com.application.al_safar.domain.repository.BookingRepository
import java.io.File
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        bookingId: Int,
        documentType: String,
        file: File
    ): Result<BookingDocument> {
        return repository.uploadDocument(bookingId, documentType, file)
    }
}
