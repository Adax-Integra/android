package com.example.adaxintegra.data.remote.dto

import com.example.adaxintegra.domain.model.RecordDocuments
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

// V-13: response of GET api/internal-users/external-users/{userId}/documents
data class RecordDocumentsResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: RecordDocumentsDataDto?,
)

data class RecordDocumentsDataDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("identity_document")
    val identityDocument: RecordDocumentDto?,
    @SerializedName("proof_of_address")
    val proofOfAddress: RecordDocumentDto?,
)

data class RecordDocumentDto(
    @SerializedName("url")
    val url: String?,
    @SerializedName("file_type")
    val fileType: String?,
)
