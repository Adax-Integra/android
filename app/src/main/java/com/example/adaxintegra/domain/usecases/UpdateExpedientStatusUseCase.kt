package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.ExpedientRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UpdateExpedientStatusUseCase @Inject constructor(
    private val expedientRepository: ExpedientRepository,
) {
    suspend operator fun invoke(recordId: String, status: String): Result<Unit> {
        return expedientRepository.updateExpedientStatus(recordId, status)
    }
}
