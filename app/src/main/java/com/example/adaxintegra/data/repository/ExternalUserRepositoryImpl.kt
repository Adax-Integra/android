package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.ExternalUserApi
import com.example.adaxintegra.data.remote.dto.ReviewVerificationRequestDto
import com.example.adaxintegra.domain.model.ExternalUserPage
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import javax.inject.Inject
import javax.inject.Singleton

// G-16: lists the external accounts in the backend
@Singleton
class ExternalUserRepositoryImpl @Inject constructor(
    private val api: ExternalUserApi,
    private val authRepository: AuthRepository,
) : ExternalUserRepository {
    // Reads the current token from the login session
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para continuar")
        }
        return "Bearer $token"
    }

    override suspend fun getExternalUsers(page: Int, search: String, status: String): ExternalUserPage {
        val response = api.getExternalUsers(
            authorization = authorizationHeader(),
            page = page,
            // An empty search is not sent
            search = search.ifBlank { null },
            status = status,
            // Newest accounts first
            sort = "recent",
        )
        val data = response.data ?: throw IllegalStateException("Empty response from server")
        return data.toDomain()
    }

    override suspend fun getExternalProfile(userId: String): ExternalUserProfile {
        val response = api.getExternalProfile(
            authorization = authorizationHeader(),
            userId = userId,
        )
        val data = response.data ?: throw IllegalStateException("Empty response from server")
        return data.toDomain()
    }

    override suspend fun updateExternalProfile(
        userId: String,
        update: ExternalUserProfileUpdate,
    ): ExternalUserProfile {
        val response = api.updateExternalProfile(
            authorization = authorizationHeader(),
            userId = userId,
            request = update.toRequestDto(),
        )
        val data = response.data ?: throw IllegalStateException("Empty response from server")
        return data.toDomain()
    }

    override suspend fun reviewVerification(
        userId: String,
        status: String,
        reason: String?,
    ): String {
        val response = api.reviewVerification(
            authorization = authorizationHeader(),
            userId = userId,
            request = ReviewVerificationRequestDto(status = status, reason = reason),
        )
        return response.data?.verificationStatus
            ?: throw IllegalStateException("Empty response from server")
    }
}
