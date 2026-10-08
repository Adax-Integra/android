package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// Matches the succesful response from GET /api/records
data class RecordListResponseDto(
    val success: Boolean,
    val data: RecordListDataDto,
)

// Pagination is calculated by the backend after applying filters
data class RecordListDataDto(
    val records: List<RecordListItemDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
)

// maps API field names to Kotlin naming conventions
data class RecordListItemDto(
    @SerializedName("record_id")
    val recordId: String,

    @SerializedName("user_id")
    val userId: String,

    val name: String,

    @SerializedName("record_number")
    val recordNumber: String?,

    val status: String,

    @SerializedName("active_cases_count")
    val activeCasesCount: Int,

    @SerializedName("updated_at")
    val updatedAt: String?,
)
