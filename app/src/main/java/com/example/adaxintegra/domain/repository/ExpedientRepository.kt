package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.PersonalDataForm

interface ExpedientRepository {
    suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<Unit>
}
