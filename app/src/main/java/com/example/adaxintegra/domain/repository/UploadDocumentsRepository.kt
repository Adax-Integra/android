package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.UploadDocumentFile

interface UploadDocumentsRepository {
    suspend fun uploadDocuments(
        identityDocument: UploadDocumentFile?,
        proofOfAddress: UploadDocumentFile?,
    )
}
