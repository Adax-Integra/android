package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.EventApi
import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.EventRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventApi,
) : EventRepository {
    override suspend fun createEvent(token: String, event: NewEvent): Result<String> {
        return try {
            val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = CreateEventRequestDto(
                title = event.title,
                startDate = event.startDate,
                endDate = event.endDate,
                startTime = event.startTime,
                endTime = event.endTime,
                modality = event.modality,
                location = event.location,
                link = event.link.takeIf { !it.isNullOrBlank() },
                phone = event.phone.takeIf { !it.isNullOrBlank() },
                description = event.description,
                notes = event.notes.takeIf { !it.isNullOrBlank() },
            )
            val response = api.createEvent(authHeader, request)
            val eventId = response.data?.eventId
            if (response.success && !eventId.isNullOrBlank()) {
                Result.success(eventId)
            } else {
                Result.failure(Exception("No se pudo obtener el ID del evento creado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadEventImage(
        token: String,
        eventId: String,
        imagePart: MultipartBody.Part,
    ): Result<Unit> {
        return try {
            val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.uploadEventImage(authHeader, eventId, imagePart)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se pudo subir la imagen del evento"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
