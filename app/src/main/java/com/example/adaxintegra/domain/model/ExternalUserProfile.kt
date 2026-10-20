package com.example.adaxintegra.domain.model

data class ExternalUserProfile(
    val userId: String,
    val verificationStatus: String?,
    val name: String?,
    val lastName: String?,
    val email: String?,
    val birthDate: String?,
    val phone: String?,
    val country: String?,
    val state: String?,
    val municipality: String?,
)

data class ExternalUserProfileUpdate(
    val email: String? = null,
    val phone: String? = null,
    val country: String? = null,
    val state: String? = null,
    val municipality: String? = null,
    val reason: String,
    val consentConfirmed: Boolean,
)
