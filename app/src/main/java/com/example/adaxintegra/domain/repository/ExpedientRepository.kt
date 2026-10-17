package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.PersonalDataForm

interface ExpedientRepository {
    suspend fun updateExpedientStatus(recordId: String, status: String): Result<Unit>
    suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<String>
}
