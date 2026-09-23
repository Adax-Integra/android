package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// File that manages the network's logic, catches errors and allows the operation state
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Result<String>> = flow {
        // 1 Emit the loading state
        emit(Result.Loading)
        try {
            // 2 Realize the petition to the backend
            val response = api.login(LoginRequestDto(email, password))

            // 3 If success, emit the token
            emit(Result.Success(response.token))
        } catch (e: Exception) {
            // 4 If failure, (throw error {red, 401, etc.})
            emit(Result.Error(e))
        }
    }
}
