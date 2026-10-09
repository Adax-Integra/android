package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.data.remote.dto.PolicyDataDto

interface PrivacyPolicyRepository {
    suspend fun getPrivacyConsent(): Boolean
    suspend fun getCurrentPolicy(): PolicyDataDto?
    suspend fun acceptPrivacyPolicy(policyId: String)
}
