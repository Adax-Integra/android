package com.example.adaxintegra.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.example.adaxintegra.domain.model.UploadDocumentFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.roundToInt

// R-06: Every picture is sent as JPEG
// ImageDecoder reads JPEG, PNG and HEIC and applies the EXIF rotation
// Encoding the picture again also leaves out its EXIF data (GPS location
// included) and turns a 4 MB photo into a few hundred KB, so the upload
// ends long before the timeout.
class DocumentImageEncoder @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun encode(
        uri: Uri,
        fileName: String,
    ): UploadDocumentFile = withContext(Dispatchers.IO) {
        val bitmap = try {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, uri),
            ) { decoder, info, _ ->
                val longestSide = max(info.size.width, info.size.height)

                if (longestSide > MAX_IMAGE_SIDE_PX) {
                    val scale = MAX_IMAGE_SIDE_PX.toFloat() / longestSide
                    decoder.setTargetSize(
                        (info.size.width * scale).roundToInt().coerceAtLeast(1),
                        (info.size.height * scale).roundToInt().coerceAtLeast(1),
                    )
                }
                // Hardware bitmaps can not be compressed to JPEG
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("No se pudo abrir la imagen seleccionada.")
        }

        val bytes = ByteArrayOutputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
            output.toByteArray()
        }
        bitmap.recycle()

        // Same limit as the backend
        if (bytes.size > MAX_DOCUMENT_BYTES) {
            throw IllegalArgumentException("El documento no debe pesar más de 5 MB.")
        }

        UploadDocumentFile(
            fileName = fileName,
            mimeType = "image/jpeg",
            bytes = bytes,
        )
    }

    private companion object {
        // Enough to read an INE, a 12 MP photo is about 4000 px wide
        const val MAX_IMAGE_SIDE_PX = 2048

        const val JPEG_QUALITY = 80

        const val MAX_DOCUMENT_BYTES = 5 * 1024 * 1024
    }
}
