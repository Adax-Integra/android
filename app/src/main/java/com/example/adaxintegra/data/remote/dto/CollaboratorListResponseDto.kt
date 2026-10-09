package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-06: one collaborator of the list returned by GET /api/internal-users
data class CollaboratorListItemDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    // Acceptance criteria: associated email (shown when the backend sends it)
    @SerializedName("email")
    val email: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
)

// G-06: response of the collaborators list, already sorted by creation date
data class CollaboratorListResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<CollaboratorListItemDto>?,
)
