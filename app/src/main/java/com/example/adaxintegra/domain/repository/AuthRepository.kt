package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining authentication contracts for Supabase Auth and remote API services.
 * Covers user registration, login, session observation, email verification OTP, and logout.
 */
interface AuthRepository {
    // Exposes current user session changes. A null value indicates no active user session.
    val session: StateFlow<UserSession?>

    // // G-09-Register: Executes user login and validates email confirmation status
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Clears local and active user session state
    fun logout()

    // // G-09-Register: Registers a new user account with Supabase Auth and remote backend
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // // G-09-VerifyOTP: Resends the 6-digit confirmation email OTP code to the specified email
    fun resendVerificationEmail(email: String): Flow<Result<Unit>>

    // // G-09-VerifyOTP: Verifies the 6-digit OTP code entered by the user against Supabase Auth
    fun verifyEmailCode(
        email: String,
        code: String,
    ): Flow<Result<UserSession>>
}
