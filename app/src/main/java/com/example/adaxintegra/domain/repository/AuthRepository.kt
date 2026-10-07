package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    // Exposes session changes. A null value means no user is signed in.
    val session: StateFlow<UserSession?>

    // Emits state while signing in
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    //no session is needed for this action
    suspend fun forgotPassword(
        email: String
    ): Result<Unit>

    suspend fun resetPassword(
        token: String,
        newPassword: String
    ): Result<Unit>

    // Clears the session
    fun logout()

    // register
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>>
}
