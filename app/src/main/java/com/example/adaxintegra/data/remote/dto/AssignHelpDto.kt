package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// R-08: request to assign helps to a case
data class AssignHelpRequestDto(
    @SerializedName("helpIds")
    val helpIds: List<String>
)

// R-08: response from assigning helps to a case
data class AssignHelpResponseDto(
    val success: Boolean,
    val message: String? = null
)
