package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.EventApi
import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventApi,
) : EventRepository {
    override suspend fun createEvent(token: String, event: NewEvent): Result<Unit> {
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
            api.createEvent(authHeader, request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
