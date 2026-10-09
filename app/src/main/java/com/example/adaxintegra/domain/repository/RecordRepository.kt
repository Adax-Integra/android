package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.entities.RecordPage

interface RecordRepository {
    // Exposes record retrieval
    suspend fun getRecords(
        page: Int = 1,
        search: String = "",
        hasOpenCases: Boolean? = null,
        status: String? = null,
    ): RecordPage
}
