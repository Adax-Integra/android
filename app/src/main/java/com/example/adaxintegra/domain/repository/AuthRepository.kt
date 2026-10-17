package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Auth contracts. Every flow ends in Result.Error(AuthError) when it fails
interface  AuthRepository {
    // Exposes session changes. Null value means no user is signed in
    val session: StateFlow<UserSession?>

    // G-02: signs in and saves the session on the device
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Restores the session saved on device
    suspend fun restoreSession()

    // Clears the session from storage and memory
    suspend fun logout()

    //no session is needed for this action
    suspend fun forgotPassword(
        email: String
    ): Result<Unit>

    suspend fun validateRecoveryCode(
        email: String,
        code: String
    ): Result<Unit>

    suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String
    ): Result<Unit>

    // G-01: creates the account; the backend emails a 6 digit code.
    // There is no session until the email is verified (G-09)
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<Unit>>

    // G-09: asks the backend for a new verification code
    fun resendVerificationEmail(email: String): Flow<Result<Unit>>

    // G-09: verifies the code; on success the user is signed in
    fun verifyEmailCode(email: String, code: String): Flow<Result<UserSession>>

    // G-12: change password
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Result<String>>
}
