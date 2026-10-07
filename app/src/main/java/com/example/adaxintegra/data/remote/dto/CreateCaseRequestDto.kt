package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.descriptors.SerialDescriptor

//R-02: body sent to POST api/external-users/{userId}/cases
//The backend reads these three names in camelCase (createCaseValidator.js)
data class CreateCaseRequestDto(
    @SerializedName("writtenDescription")
    val writtenDescription: String,
    @SerializedName("writtenHelpsWanted")
    val writtenHelpsWanted: String,
    @SerializedName("hasExternalSupport")
    val hasExternalSupport: Boolean,
)
