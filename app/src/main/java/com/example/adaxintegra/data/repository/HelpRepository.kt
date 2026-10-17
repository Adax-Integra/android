package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.HelpApi
import com.example.adaxintegra.domain.model.HelpType
import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpRepository @Inject constructor(
    private val helpApi: HelpApi,
    private val authRepository: AuthRepository,
) {
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para consultar las ayudas")
        }
        return "Bearer $token"
    }

    suspend fun getHelpTypes(): List<HelpType> {
        val response = helpApi.getHelpTypes(authorizationHeader())
        if (response.success) {
            return response.data.map { dto ->
                HelpType(
                    helpId = dto.helpId,
                    description = dto.description
                )
            }
        } else {
            throw Exception("Failed to load help types.")
        }
    }
}
