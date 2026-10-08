package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * // G-09-VerifyOTP: UseCase for validating the 6-digit email confirmation OTP code with Supabase Auth.
 */
class VerifyEmailCodeUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    // Executes the OTP code verification for the given email address
    operator fun invoke(email: String, code: String): Flow<Result<UserSession>> =
        repository.verifyEmailCode(email, code)
}
