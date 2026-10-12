package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.remote.dto.PolicyDataDto
import com.example.adaxintegra.domain.repository.PrivacyPolicyRepository
import javax.inject.Inject

class GetCurrentPrivacyPolicyUseCase @Inject constructor(
    private val repository: PrivacyPolicyRepository,
) {
    suspend operator fun invoke(): PolicyDataDto? = repository.getCurrentPolicy()
}
