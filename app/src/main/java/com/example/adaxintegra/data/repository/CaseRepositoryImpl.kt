package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.CaseApi
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

    override suspend fun getAllCasesFromUser(userId: String): List<Case> {
        val response = api.getCasesFromUser(userId)
        return response.data.map { it.toDomain() }
    }
}
