package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.domain.entities.CasePage
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.CaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaseRepositoryImpl
@Inject
constructor(
    private val api: CaseApi,
    private val authRepository: AuthRepository,
) : CaseRepository {

    // Reads the current token whenever a request is made
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar los casos",
            )
        }
        return "Bearer $token"
    }

    override suspend fun getCaseById(caseId: String): Case {
        val response = api.getCaseById(
            caseId = caseId,
            authorization = authorizationHeader(),
        )
        return response.data.toDomain()
    }

    override suspend fun getCases(
        search: String,
        urgency: String,
        page: Int,
        limit: Int,
    ): CasePage {
        val response = api.getCases(
            authorization = authorizationHeader(),
            search = search,
            urgency = urgency,
            page = page,
            limit = limit,
        )

        if (!response.success) {
            throw IllegalStateException("Unable to retrive cases.")
        }
        val data = response.data

        return CasePage(
            cases = data.cases.map { it.toDomain() },
            total = data.total,
            page = data.page,
            limit = data.limit,
        )
    }

    override suspend fun getCasesFromUser(userId: String): List<Case> {
        val response = api.getCasesFromUser(
            userId = userId,
            authorization = authorizationHeader(),
        )
        return response.data.map { it.toDomain() }
    }

    override suspend fun getExternalUserCases(userId: String): List<Case> {
        val response = api.getExternalUserCases(
            userId = userId,
            authorization = authorizationHeader(),
        )
        return response.data.map { it.toDomain() }
    }
}
