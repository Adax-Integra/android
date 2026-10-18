package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.ExternalUserApi
import com.example.adaxintegra.domain.model.ExternalUserPage
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
}
