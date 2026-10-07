package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// R-01 Register user
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = repository.register(name, lastname, phone, email, password)
}
