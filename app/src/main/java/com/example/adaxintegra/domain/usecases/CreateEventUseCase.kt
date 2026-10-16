package com.example.adaxintegra.domain.usecases

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.EventRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository,
    @ApplicationContext private val context: Context,
) {
    operator fun invoke(token: String, event: NewEvent, imageUri: Uri?): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            // Step 1: Create event
            val createResult = repository.createEvent(token, event)
            if (createResult.isFailure) {
                emit(Result.Error(createResult.exceptionOrNull() ?: Exception("Error al crear el evento")))
                return@flow
            }
            val eventId = createResult.getOrNull()

            // Step 2: Upload image if present
            if (!eventId.isNullOrBlank() && imageUri != null) {
                val imagePart = uriToMultipartPart(context, imageUri)
                val uploadResult = repository.uploadEventImage(token, eventId, imagePart)
                if (uploadResult.isFailure) {
                    emit(Result.Error(uploadResult.exceptionOrNull() ?: Exception("Error al subir la imagen del evento")))
                    return@flow
                }
            }

            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private fun uriToMultipartPart(
        context: Context,
        uri: Uri,
        paramName: String = "image",
    ): MultipartBody.Part {
        val contentResolver = context.contentResolver

        // Validate file size (max 5 MB) according to backend contract
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (it.moveToFirst() && sizeIndex != -1) {
                val size = it.getLong(sizeIndex)
                if (size > 5 * 1024 * 1024) {
                    throw IllegalArgumentException("La imagen supera el límite de 5 MB permitido.")
                }
            }
        }

        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        if (!mimeType.contains("jpeg", ignoreCase = true) &&
            !mimeType.contains("jpg", ignoreCase = true) &&
            !mimeType.contains("png", ignoreCase = true)
        ) {
            throw IllegalArgumentException("Solo se permiten imágenes en formato JPEG o PNG.")
        }

        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo leer la imagen seleccionada (URI inválida o inaccesible).")

        val extension = when {
            mimeType.contains("png", ignoreCase = true) -> "png"
            else -> "jpg"
        }

        val file = File(context.cacheDir, "temp_event_image_${System.currentTimeMillis()}.$extension")
        file.outputStream().use { output ->
            inputStream.use { input ->
                input.copyTo(output)
            }
        }

        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, file.name, requestBody)
    }
}
