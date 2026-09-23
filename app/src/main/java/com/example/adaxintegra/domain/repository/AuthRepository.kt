package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // defining login. Using flow to make it reactive and Result to manage error/success
    fun login(email: String, password: String): Flow<Result<String>> // return token if success
}
