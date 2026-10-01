package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.domain.repository.ExpedientRepository
import jakarta.inject.Inject

class RegisterExpedientUseCase @Inject constructor(
    private val repository: ExpedientRepository,
) {
    suspend operator fun invoke(token: String, form: PersonalDataForm): Result<Unit> = repository.registerExpedient(token, form)
}
