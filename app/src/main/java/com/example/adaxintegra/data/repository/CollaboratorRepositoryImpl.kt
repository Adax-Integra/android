package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.CollaboratorApi
import com.example.adaxintegra.data.remote.dto.UpdateCollaboratorStatusRequestDto
import com.example.adaxintegra.data.remote.dto.CollaboratorProfileFieldsDto
import com.example.adaxintegra.data.remote.dto.UpdateCollaboratorProfileRequestDto
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.CollaboratorDetail
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import javax.inject.Inject
import javax.inject.Singleton

// G-03: sends the new collaborator to the backend
// G-06: lists collaborator accounts in the backend
// G-10: gets the detail of one collaborator
// G-04: activates or deactivates a collaborator account
@Singleton
class CollaboratorRepositoryImpl @Inject constructor(
    private val api: CollaboratorApi,
    private val authRepository: AuthRepository,
) : CollaboratorRepository {
    // Reads the current token from the login session
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para continuar")
        }
        return "Bearer $token"
    }

    override suspend fun createCollaborator(collaborator: NewCollaborator): Collaborator {
        val response = api.createCollaborator(authorizationHeader(), collaborator.toRequestDto())
        val created = response.data ?: throw IllegalStateException("Empty response from server")
        return created.toDomain()
    }

    // G-06: the backend already returns the list sorted by the creation date
    override suspend fun getCollaborators(): List<CollaboratorSummary> {
        val response = api.getCollaborators(authorizationHeader())
        val items = response.data ?: emptyList()
        return items.map { it.toDomain() }
    }

    // G-10: a missing collaborator answers 404, Retrofit throws it as an HttpException
    override suspend fun getCollaboratorDetail(userId: String): CollaboratorDetail {
        val response = api.getCollaboratorDetail(authorizationHeader(), userId)
        val detail = response.data ?: throw IllegalStateException("Empty response from server")
        return detail.toDomain()
    }

    // G-04: returns the status saved by the backend
    override suspend fun updateCollaboratorStatus(userId: String, isActive: Boolean): Boolean {
        val response = api.updateCollaboratorStatus(
            authorization = authorizationHeader(),
            userId = userId,
            request = UpdateCollaboratorStatusRequestDto(isActive = isActive),
        )
        val status = response.data ?: throw IllegalStateException("Empty response from server")
        return status.isActive ?: isActive
    }

    // G-18: uses the existing session and Retrofit client
    override suspend fun updateCollaboratorProfile(
        userId: String,
        name: String?,
        lastName: String?,
        email: String?,
    ) {
        val response = api.updateCollaboratorProfile(
            authorization = authorizationHeader(),
            userId = userId,
            request = UpdateCollaboratorProfileRequestDto(
                profile = CollaboratorProfileFieldsDto(
                    name = name,
                    lastName = lastName,
                    email = email,
                ),
            ),
        )

        if (!response.success) {
            throw IllegalStateException("No se pudo actualizar la colaboradora")
        }
    }
}
