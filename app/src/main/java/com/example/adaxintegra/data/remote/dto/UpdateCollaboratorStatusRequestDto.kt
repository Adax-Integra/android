package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-04: body of PATCH api/internal-users/{userId}/status
data class UpdateCollaboratorStatusRequestDto(
    @SerializedName("is_active")
    val isActive: Boolean,
)
