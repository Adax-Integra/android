package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// G-01: register an external user; the session starts after verifying the email (G-09)
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        name: String,
        lastname: String,
        countryCode: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<Unit>> = repository.register(name, lastname, countryCode, phone, email, password)
}
