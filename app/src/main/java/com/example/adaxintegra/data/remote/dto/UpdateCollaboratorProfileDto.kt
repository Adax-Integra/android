package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// Only modified fields are serialized; Gson omits null values.
data class CollaboratorProfileFieldsDto(
    val name: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    val email: String? = null,
)

data class UpdateCollaboratorProfileRequestDto(
    val profile: CollaboratorProfileFieldsDto,
)

data class UpdateCollaboratorProfileResponseDto(
    val success: Boolean,
)