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
    @SerializedName(value = "id", alternate = ["record_id", "recordId"])
    val recordId: String,

    @SerializedName(value = "user_id", alternate = ["userId"])
    val userId: String,

    val name: String,

    @SerializedName(value = "record_number", alternate = ["recordNumber"])
    val recordNumber: String? = null,

    val status: String,

    @SerializedName(value = "active_cases_count", alternate = ["activeCasesCount"])
    val activeCasesCount: Int = 0,

    @SerializedName(value = "updated_at", alternate = ["updatedAt"])
    val updatedAt: String? = null,
)
