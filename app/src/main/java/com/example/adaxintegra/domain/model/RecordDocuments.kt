package com.example.adaxintegra.domain.model

// V-13: documents an internal user reviews inside an external user's record
data class RecordDocuments(
    val identityDocument: RecordDocument?,
    val proofOfAddress: RecordDocument?,
)

data class RecordDocument(
    // Signed URL, it expires after 15 minutes. Null when the backend could not sign it
    val url: String?,
    val fileType: RecordDocumentType,
)

enum class RecordDocumentType {
    PDF,
    IMAGE,
}
