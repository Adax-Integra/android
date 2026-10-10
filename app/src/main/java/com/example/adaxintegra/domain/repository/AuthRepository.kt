package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    // Exposes session changes. A null value means no user is signed in.
    val session: StateFlow<UserSession?>

    // Emits state while singing in
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Restores the session saved on the device.
    suspend fun restoreSession()

    // Clears the session from storage and memory.
    suspend fun logout()
    //no session is needed for this action
    suspend fun forgotPassword(
        email: String
    ): Result<Unit>

    suspend fun resetPassword(
        newPassword: String
    ): Result<Unit>

    // G-09-Register: Registers a new user account with Supabase Auth and remote backend
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // resend verification email
    fun resendVerificationEmail(email: String): Flow<Result<Unit>>

    // verify email code
    fun verifyEmailCode(email: String, code: String): Flow<Result<UserSession>>

    // change password
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Result<String>>
}
