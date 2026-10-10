package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// R-02: case returned after creating it (CreateCaseDTO), same field names as
// the listing of "Mis casos" (V-04)
data class CreatedCaseDto (
    @SerializedName("caseId")
    val caseId: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("writtenDescription")
    val writtenDescription: String?,
    @SerializedName("writtenHelpsWanted")
    val writtenHelpsWanted: String?,
    @SerializedName("hasLawyer")
    val hasLawyer: Boolean?,
    @SerializedName("helps")
    val helps: List<String>?,
)

data class CreateCaseResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: CreatedCaseDto,
)

//R-02 body of a rejected request
//({success, error, errors: { campo:motivo } })
data class CreateCaseErrorDto(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("errors")
    val errors: Map<String, String>?,
)
