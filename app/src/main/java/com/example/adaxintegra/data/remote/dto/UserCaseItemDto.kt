package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AssignedUserDto(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
)

// case item returned by the user cases endpoints
data class UserCaseItemDto(
    @SerializedName(value = "case_id", alternate = ["caseId"])
    val caseId: String?,
    @SerializedName(value = "case_number", alternate = ["caseNumber"])
    val caseNumber: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName(value = "written_description", alternate = ["writtenDescription"])
    val writtenDescription: String?,
    @SerializedName(value = "written_helps_wanted", alternate = ["writtenHelpsWanted"])
    val writtenHelpsWanted: String?,
    @SerializedName(value = "has_lawyer", alternate = ["hasLawyer"])
    val hasLawyer: Boolean?,
    @SerializedName(value = "created_at", alternate = ["createdAt"])
    val createdAt: String?,
    @SerializedName(value = "updated_at", alternate = ["updatedAt"])
    val updatedAt: String?,
    @SerializedName("helps")
    val helps: List<String>?,
    @SerializedName(value = "violence_types", alternate = ["violenceTypes"])
    val violenceTypes: List<String>?,
    @SerializedName(value = "assigned_users", alternate = ["assignedUsers", "interns_assigned", "internsAssigned"])
    val assignedUsers: List<AssignedUserDto>?,
)
