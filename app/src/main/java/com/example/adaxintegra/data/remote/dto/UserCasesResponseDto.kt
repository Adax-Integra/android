package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// response of the user cases endpoints (V-04 and V-10): plain list, no pagination
data class UserCasesResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<UserCaseItemDto>,
)
