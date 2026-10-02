package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    // Clears the session stored in memory
    operator fun invoke() {
        repository.logout()
    }
}
