package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.PrivacyPolicyRepository
import javax.inject.Inject

class AcceptPrivacyConsentUseCase @Inject constructor(
    private val repository: PrivacyPolicyRepository,
) {
    suspend operator fun invoke(policyId: String) = repository.acceptPrivacyPolicy(policyId)
}
