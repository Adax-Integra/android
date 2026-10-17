package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// R-07: request to assign violences to a case
data class AssignViolenceRequestDto(
    @SerializedName(value = "violenceIds", alternate = ["violence_ids"])
    val violenceIds: List<String>
)

// R-07: response from assigning violences to a case
data class AssignViolenceResponseDto(
    val success: Boolean,
    val data: AssignViolenceDataDto? = null,
    val error: String? = null,
    val message: String? = null
)

data class AssignViolenceDataDto(
    @SerializedName(value = "caseId", alternate = ["case_id"])
    val caseId: String? = null,

    @SerializedName(value = "violenceIds", alternate = ["violence_ids"])
    val violenceIds: List<String> = emptyList(),

    val urgency: String? = null
)
