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

    // Clears the session
    fun logout()
}
