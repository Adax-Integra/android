package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.data.remote.dto.CloseCaseDataDto
import com.example.adaxintegra.domain.model.CaseDetail
import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

// repository used exclusively by the V-11 case detail flow
@Singleton
class CaseDetailRepository
@Inject
constructor(
    private val api: CaseApi,
    private val authRepository: AuthRepository,
) {
    // reads the current session token before requesting case information
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar el detalle del caso",
            )
        }

        return "Bearer $token"
    }

    // retrieves the complete case detail required by V-11
    suspend fun getCaseDetail(caseId: String): CaseDetail {
        val response =
            api.getCaseDetail(
                caseId = caseId,
                authorization = authorizationHeader(),
            )

        if (!response.success) {
            throw IllegalStateException(
                "Unable to retrieve case detail.",
            )
        }

        return response.data.toDomain()
    }

    // closes the selected case through the V-11 backend endpoint
    suspend fun closeCase(caseId: String): CloseCaseDataDto {
        val response =
            api.closeCase(
                caseId = caseId,
                authorization = authorizationHeader(),
            )

        if (!response.success) {
            throw IllegalStateException(
                "Unable to close case.",
            )
        }

        return response.data
    }
}
