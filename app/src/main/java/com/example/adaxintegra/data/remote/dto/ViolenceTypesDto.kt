package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// R-07: response when getting all available violence types
data class ViolenceTypesResponseDto(
    val success: Boolean,
    val data: List<ViolenceTypeItemDto> = emptyList()
)

data class ViolenceTypeItemDto(
    @SerializedName(value = "violence_id", alternate = ["violenceId", "id", "help_id", "helpId"])
    val violenceId: String? = null,

    @SerializedName(value = "description", alternate = ["type", "name"])
    val description: String? = null,

    @SerializedName(value = "severity")
    val severity: Int? = null
)
