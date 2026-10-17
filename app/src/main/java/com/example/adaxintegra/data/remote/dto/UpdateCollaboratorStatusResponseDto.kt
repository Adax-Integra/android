package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-04: new status of the collaborator returned by the backend
data class CollaboratorStatusDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
)

// G-04: response of PATCH api/internal-users/{userId}/status
data class UpdateCollaboratorStatusResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: CollaboratorStatusDto?,
)
