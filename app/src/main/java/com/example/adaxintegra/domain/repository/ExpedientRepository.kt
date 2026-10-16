package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.PersonalDataForm
import kotlinx.coroutines.flow.Flow

interface ExpedientRepository {
    suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<Unit>
    suspend fun updateExpedientStatus(recordId: String, status: String): Result<Unit>
}
