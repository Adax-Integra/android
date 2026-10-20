package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.ViolenceApi
import com.example.adaxintegra.domain.model.Violence
import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViolenceRepository @Inject constructor(
    private val violenceApi: ViolenceApi,
    private val authRepository: AuthRepository,
) {
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para consultar los tipos de violencia")
        }
        return "Bearer $token"
    }

    suspend fun getViolenceTypes(): List<Violence> {
        val response = violenceApi.getViolenceTypes(authorizationHeader())
        if (response.success) {
            return response.data.mapNotNull { dto ->
                val id = dto.violenceId?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val type = dto.description?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                Violence(
                    id = id,
                    type = type,
                    severity = dto.severity ?: 0
                )
            }
        } else {
            throw Exception("Failed to load violence types.")
        }
    }
}
