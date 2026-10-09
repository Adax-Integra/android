package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-03: collaborator returned by the backend after creating the account
data class CollaboratorDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone : String?,
    @SerializedName("role")
    val role: String?,
)

data class CreateCollaboratorResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: CollaboratorDto?,
)
