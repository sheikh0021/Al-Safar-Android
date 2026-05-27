package com.application.al_safar.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File

object BookingFileUtils {

    fun copyUriToCacheFile(
        context: Context,
        uri: Uri,
        prefix: String
    ): File {
        val extension = context.contentResolver.getType(uri)?.substringAfterLast("/") ?: "bin"
        val file = File.createTempFile(prefix, ".$extension", context.cacheDir)

        context.contentResolver.openInputStream(uri).use { input ->
            file.outputStream().use { output ->
                requireNotNull(input) {"Unable to open selected file."}
                input.copyTo(output)
            }
        }
        return file
    }
    fun createCameraImageFile(context: Context, documentType: String): File {
        return File.createTempFile(
            "camera_${documentType}_",
            ".jpg",
            context.cacheDir
        )
    }

    fun getUriForFile(context: Context, file:File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    private fun getDisplayName(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return "document.bin"
    }
}