package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Case

interface CaseRepository {
    suspend fun getCaseById(caseId: String): Case

    suspend fun getCasesFromUser(userId: String): List<Case>
}
