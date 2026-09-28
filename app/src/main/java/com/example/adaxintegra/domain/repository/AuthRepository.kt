package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<UserSession>>
}
