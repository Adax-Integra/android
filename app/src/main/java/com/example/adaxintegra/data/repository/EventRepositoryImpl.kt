package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toCreateDto
import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.EventApi
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.EventRepository
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
}
