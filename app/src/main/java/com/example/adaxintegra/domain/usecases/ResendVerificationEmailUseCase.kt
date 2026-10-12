package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResendVerificationEmailUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(email: String): Flow<Result<Unit>> = repository.resendVerificationEmail(email)
}
