package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.CollaboratorApi
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import javax.inject.Inject
import javax.inject.Singleton

// G-03: sends the new collaborator to the backend
@Singleton
class CollaboratorRepositoryImpl @Inject constructor(
    private val api: CollaboratorApi,
    private val authRepository: AuthRepository,
) : CollaboratorRepository {
    // Reads the current token from the login session
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para agregar colaboradoras")
        }
        return "Bearer $token"
    }

    override suspend fun createCollaborator(collaborator: NewCollaborator): Collaborator {
        val response = api.createCollaborator(authorizationHeader(), collaborator.toRequestDto())
        val created = response.data ?: throw IllegalStateException("Empty response from server")
        return created.toDomain()
    }
}
