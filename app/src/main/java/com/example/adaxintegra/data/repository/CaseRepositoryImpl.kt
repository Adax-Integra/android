package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import com.example.adaxintegra.data.mapper.CaseMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaseRepositoryImpl @Inject constructor(
    private val api: CaseApi
) : CaseRepository {

    override suspend fun getCaseById(caseId: String): Case {
        return api.getCaseById(caseId).toDomain()
    }
}