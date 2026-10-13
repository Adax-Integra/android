package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrentPrivacyPolicyResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: PolicyDataDto? = null,
)

data class PolicyDataDto(
    @SerializedName("policyId")
    val policyId: String? = null,
    @SerializedName("version")
    val version: String? = null,
    @SerializedName("documentUrl")
    val documentUrl: String? = null,
    @SerializedName("content")
    val content: String? = null,
)

data class PrivacyConsentResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val consentData: ConsentDataDto? = null,
)

data class ConsentDataDto(
    @SerializedName("hasAccepted")
    val hasAccepted: Boolean,
    @SerializedName("acceptedAt")
    val acceptedAt: String? = null,
    @SerializedName("version")
    val version: String? = null,
)

data class AcceptConsentRequestDto(
    @SerializedName("policyId")
    val policyId: String,
)

data class AcceptConsentResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val acceptedConsentData: AcceptedConsentDataDto? = null,
)

data class AcceptedConsentDataDto(
    @SerializedName("consentId")
    val consentId: String? = null,
    @SerializedName("version")
    val version: String? = null,
    @SerializedName("acceptedAt")
    val acceptedAt: String? = null,
)
