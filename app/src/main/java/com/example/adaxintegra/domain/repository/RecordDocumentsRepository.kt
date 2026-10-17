package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.RecordDocuments

interface RecordDocumentsRepository {
    suspend fun getRecordDocuments(userId: String): RecordDocuments

    suspend fun downloadDocument(url: String): ByteArray
}
