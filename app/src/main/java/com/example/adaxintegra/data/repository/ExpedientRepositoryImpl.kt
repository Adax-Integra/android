package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDto
import com.example.adaxintegra.data.remote.api.ExpedientApi
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.domain.repository.ExpedientRepository
import jakarta.inject.Inject

class ExpedientRepositoryImpl @Inject constructor(
    private val api: ExpedientApi,
) : ExpedientRepository {

    override suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<Unit> = try {
        val response = api.registerExpedient(
            authorization = "Bearer $token",
            request = form.toDto(),
        )
        if (response.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("No se pudo completar el registro del expediente."))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
