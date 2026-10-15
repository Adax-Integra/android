package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject

class ValidateRecoveryCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        code: String,
    ): Result<Unit> {
        return authRepository.validateRecoveryCode(
            email = email,
            code = code,
        )
    }
}
