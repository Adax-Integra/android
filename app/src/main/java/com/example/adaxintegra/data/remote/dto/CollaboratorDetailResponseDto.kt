package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-10: detail of one collaborator returned by GET /api/internal-users/{userId}
data class CollaboratorDetailDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
)

// G-10: response of the collaborator detail
data class CollaboratorDetailResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: CollaboratorDetailDto?,
)
