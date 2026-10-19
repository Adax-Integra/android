package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// B-02: body of PATCH /internal-users/external-users/:userId/verification
// Gson does not send a null reason, the backend saves it as null
data class ReviewVerificationRequestDto(
    val status: String,
    val reason: String? = null,
)

data class VerificationReviewResponseDto(
    val success: Boolean,
    val data: VerificationReviewDataDto?,
)

data class VerificationReviewDataDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("verification_status") val verificationStatus: String?,
)
