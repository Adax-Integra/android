package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// Error body the backend sends with 4xx/5xx
data class ApiErrorDto(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("error")
    val error: String? = null,
)
