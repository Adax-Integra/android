package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-16: one external account of the list
data class ExternalUserSummaryDto(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
)

// G-16: one page of the list, the backend sends 10 accounts per page
data class ExternalUserListDataDto(
    @SerializedName("users")
    val users: List<ExternalUserSummaryDto>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("limit")
    val limit: Int?,
)

// G-16: response of GET api/internal-users/external-users
data class ExternalUserListResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: ExternalUserListDataDto?,
)
