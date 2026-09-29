package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.CollaboratorApi
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import javax.inject.Inject
import javax.inject.Singleton

// G-03: sends the new collaborator to the backend
@Singleton
class CollaboratorRepositoryImpl @Inject constructor(
    private val api : CollaboratorApi,
) : CollaboratorRepository {
    override suspend fun createCollaborator(token: String, collaborator: NewCollaborator): Collaborator {
        val response = api.createCollaborator("Bearer $token", collaborator.toRequestDto())
        val created = response.data ?: throw IllegalStateException("Empty response from server")
        return created.toDomain()
    }
}
