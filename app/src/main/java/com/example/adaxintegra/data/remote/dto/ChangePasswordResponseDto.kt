package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangePasswordDataDto(
    @SerializedName("message")
    val message: String? = null,
)

data class ChangePasswordResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ChangePasswordDataDto? = null,

    @SerializedName("error")
    val error: String? = null,
)
