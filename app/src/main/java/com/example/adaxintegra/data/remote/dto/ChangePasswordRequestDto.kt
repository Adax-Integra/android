package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestDto(
    @SerializedName("current_password")
    val currentPassword: String,

    @SerializedName("new_password")
    val newPassword: String,

    @SerializedName("confirm_password")
    val confirmPassword: String,
)
