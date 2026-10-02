package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-03: body sent to POST api/internal-users
data class CreateCollaboratorRequestDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String?,
)
