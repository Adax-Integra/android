package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.RecordApi
import com.example.adaxintegra.data.remote.dto.UpdateExpedientStatusRequestDto
import com.example.adaxintegra.domain.entities.RecordPage
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.RecordRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val api: RecordApi,
    private val authRepository: AuthRepository,
) : RecordRepository {

    // Reads the current session token for each request
    private suspend fun authorizationHeader(): String {
        val session = authRepository.session.filterNotNull().first()
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar los expedientes.",
            )
        }
        return "Bearer $token"
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

    suspend fun updateRecordStatus(recordId: String, status: String) {
        val request = UpdateExpedientStatusRequestDto(status = status)
        val response = api.updateRecordStatus(
            recordId = recordId,
            authorization = authorizationHeader(),
            body = request
        )

        if (!response.success) {
            throw IllegalStateException(response.message ?: "No se pudo actualizar el estatus.")
        }
    }
}
