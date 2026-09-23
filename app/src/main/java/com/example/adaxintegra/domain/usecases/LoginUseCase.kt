package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Result<String>> {
        // include the validations such as the email format...
        // before calling repository
        return repository.login(email, password)
    }
}
