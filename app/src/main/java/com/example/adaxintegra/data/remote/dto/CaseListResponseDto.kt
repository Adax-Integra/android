package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// Succesful response from the case listing endpoint
data class CaseListResponseDto(
    val success: Boolean,
    val data: CaseListDataDto,
)

// List of cases and pagination information
data class CaseListDataDto(
    val cases: List<CaseListItemDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
)

data class CaseListItemDto(
    @SerializedName("case_id")
    val caseId: String,

    val name: String,

    @SerializedName("violence_types")
    val violenceTypes: List<String>,

    val state: String,
    val severity: Int?,
    val urgency: String,

    @SerializedName("updated_at")
    val updatedAt: String?,
)
