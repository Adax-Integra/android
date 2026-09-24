package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.domain.entities.CasePage
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaseRepositoryImpl
@Inject
constructor(
    private val api: CaseApi,
) : CaseRepository {
    override suspend fun getCaseById(caseId: String): Case {
        val response = api.getCaseById(caseId)
        return response.data.toDomain()
    }

    override suspend fun getCases(
        search: String,
        urgency: String,
        page: Int,
        limit: Int,
    ): CasePage {
        val response = api.getCases(
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
        val response = api.getCasesFromUser(userId)
        return response.data.map { it.toDomain() }
    }
}
