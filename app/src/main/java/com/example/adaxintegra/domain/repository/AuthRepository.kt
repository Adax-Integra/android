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
    // Exposes session changes. A null value means no user is signed in.
    val session: StateFlow<UserSession?>

    // // G-09-Register: Executes user login and validates email confirmation status
    // Emits state while singing in
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Clears local and active user session state
    // Clears the session
    fun logout()

    // // G-09-Register: Registers a new user account with Supabase Auth and remote backend
    // register
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // change password
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Result<String>>
}
