package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toCreateDto
import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.EventApi
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.EventRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

// E-01: consults the active events in the backend
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val api: EventApi,
    private val authRepository: AuthRepository,
) : EventRepository {
    // Reads the current token from the login session
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para continuar")
        }
        return "Bearer $token"
    }

    // E-01: the backend already sorts the events by start date.
    // Events whose dates cannot be read are skipped by the mapper
    override suspend fun getEvents(scope: EventScope): List<Event> {
        val response = api.getEvents(authorizationHeader(), scope.query)
        val items = response.data ?: emptyList()
        return items.mapNotNull { it.toDomain() }
    }

    //disable events
    override suspend fun disableEvent(eventId: String) {
        val response = api.disableEvent(
            eventId = eventId,
            authorizationHeader()
        )

        //throw an error even if theres data but the event id is missing
        if (!response.success || response.data?.eventId == null) {
            throw IllegalStateException(
                "No se pudo desactivar el evento"
            )
        }
    }

    override suspend fun createEvent(event: Event): Event {
        val response = api.createEvent(
            authorization = authorizationHeader(),
            request = event.toCreateDto(),
        )
        val data = response.data
            ?: throw IllegalStateException(response.error ?: "Error al registrar el evento")
        return data.toDomain()
            ?: throw IllegalStateException("Error al procesar la respuesta del servidor")
    }

    // E-02: sends the whole event again, the backend validates it like a new one
    override suspend fun updateEvent(event: Event): Event {
        val response = api.updateEvent(
            authorization = authorizationHeader(),
            eventId = event.id,
            request = event.toCreateDto(),
        )
        val data = response.data
            ?: throw IllegalStateException(response.error ?: "Error al actualizar el evento")
        // The PUT response has no image, so the current one is kept
        val domainEvent = data.toDomain()
            ?: throw IllegalStateException("Error al procesar la respuesta del servidor")
        return domainEvent.copy(imageUrl = event.imageUrl)
    }

    override suspend fun uploadEventImage(eventId: String, imageFile: UploadDocumentFile): String {
        val requestBody = imageFile.bytes.toRequestBody(imageFile.mimeType.toMediaType())
        val part = MultipartBody.Part.createFormData("image", imageFile.fileName, requestBody)

        val response = api.uploadEventImage(
            authorization = authorizationHeader(),
            eventId = eventId,
            image = part,
        )

        if (!response.success) {
            throw IllegalStateException(response.error ?: "Error al subir la imagen del evento")
        }

        val imageUrl = response.data?.imageUrl
            ?: throw IllegalStateException("Error al subir la imagen del evento")

        return imageUrl
    }
}
