package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.model.UploadDocumentFile

interface UploadDocumentsRepository {
    // R-06: documents the external user already uploaded, null when it was never uploaded
    suspend fun getDocuments(): RecordDocuments

    // The document that is null is not sent and stays the same in the backend
    suspend fun uploadDocuments(
        identityDocument: UploadDocumentFile?,
        proofOfAddress: UploadDocumentFile?,
    )

    // Bytes of one document from its signed URL
    suspend fun downloadDocument(url: String): ByteArray
}
