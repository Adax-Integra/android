package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// V-06: one entry of the activity log returned by the backend
data class ActivityLogItemDto(
    @SerializedName("log_id")
    val logId: String?,
    @SerializedName("actor_name")
    val actorName: String?,
    @SerializedName("action")
    val action: String?,
    @SerializedName("action_label")
    val actionLabel: String?,
    @SerializedName("entity")
    val entity: String?,
    @SerializedName("entity_id")
    val entityId: String?,
    @SerializedName("target")
    val target: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("created_at")
    val createdAt: String?,
)

// V-06: one page of the activity log
data class ActivityLogDataDto(
    @SerializedName("logs")
    val logs: List<ActivityLogItemDto>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("limit")
    val limit: Int?,
)

data class ActivityLogResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: ActivityLogDataDto?,
)
