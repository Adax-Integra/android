
package com.example.adaxintegra.domain.model

data class UploadDocumentFile(
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray
)
