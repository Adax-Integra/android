package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.AuthRepository
import javax.inject.Inject

class RestoreSessionUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke() {
        repository.restoreSession()
    }
}
