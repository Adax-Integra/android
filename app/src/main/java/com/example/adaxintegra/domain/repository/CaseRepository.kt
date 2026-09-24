package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.entities.CasePage
import com.example.adaxintegra.domain.model.Case

interface CaseRepository {
    suspend fun getCaseById(caseId: String): Case

    suspend fun getCases(
        search: String = "",
        urgency: String = "",
        page: Int = 1,
        limit: Int = 20,
    ): CasePage
}
