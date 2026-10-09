package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.PrivacyPolicyRepository
import javax.inject.Inject

class CheckPrivacyConsentUseCase @Inject constructor(
    private val repository: PrivacyPolicyRepository,
) {
    suspend operator fun invoke(): Boolean = repository.getPrivacyConsent()
}
