package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.PrivacyPolicyApi
import com.example.adaxintegra.data.remote.dto.AcceptConsentRequestDto
import com.example.adaxintegra.data.remote.dto.PolicyDataDto
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.PrivacyPolicyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivacyPolicyRepositoryImpl @Inject constructor(
    private val api: PrivacyPolicyApi,
    private val authRepository: AuthRepository,
) : PrivacyPolicyRepository {

    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para continuar.")
        }
        return "Bearer $token"
    }

    override suspend fun getPrivacyConsent(): Boolean {
        val response = api.getPrivacyConsent(authorizationHeader())
        return response.consentData?.hasAccepted ?: false
    }

    override suspend fun getCurrentPolicy(): PolicyDataDto? {
        val response = api.getCurrentPrivacyPolicy(authorizationHeader())
        return response.data
    }

    override suspend fun acceptPrivacyPolicy(policyId: String) {
        api.acceptPrivacyPolicy(
            authorization = authorizationHeader(),
            request = AcceptConsentRequestDto(policyId = policyId),
        )
    }
}
