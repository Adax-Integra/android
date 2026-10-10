package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    // Clears the session from local storage and memory.
    suspend operator fun invoke() {
        repository.logout()
    }
}
