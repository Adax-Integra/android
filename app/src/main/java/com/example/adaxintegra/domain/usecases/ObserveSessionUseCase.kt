package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    // Exposes the current session and its future changes.
    operator fun invoke(): StateFlow<UserSession?> = repository.session
}
