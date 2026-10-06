package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        token: String,
        newPassword: String,
    ): Result<Unit> {
        return authRepository.resetPassword(
            token = token,
            newPassword = newPassword,
        )
    }
}
