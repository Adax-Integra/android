package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.RecordApi
import com.example.adaxintegra.domain.entities.RecordPage
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.RecordRepository
import jakarta.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val api: RecordApi,
    private val authRepository: AuthRepository,
) : RecordRepository {

    // Reads the current session token for each request
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar los expedientes.",
            )
        }
        return "Barer $token"
    }

    override suspend fun getRecords(
        page: Int,
        search: String,
        hasOpenCases: Boolean?,
        status: String?,
    ): RecordPage {
        val response = api.getRecords(
            authorization = authorizationHeader(),
            page = page,
            search = search.trim(),
            hasOpenCases = hasOpenCases,
            status = status,
        )

        // A failed response must not be presented as an empty listing
        if (!response.success) {
            throw IllegalStateException("Unable to retrieve records.")
        }

        // Preserve the backend filter, order and pagination
        return response.data.toDomain()
    }
}
