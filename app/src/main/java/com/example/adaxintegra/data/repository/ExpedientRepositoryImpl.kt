package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDto
import com.example.adaxintegra.data.remote.api.ExpedientApi
import com.example.adaxintegra.data.remote.dto.UpdateExpedientStatusRequestDto
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.domain.repository.ExpedientRepository
import jakarta.inject.Inject

class ExpedientRepositoryImpl @Inject constructor(
    private val api: ExpedientApi,
) : ExpedientRepository {

    override suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<String> = try {
        val response = api.registerExpedient(
            authorization = "Bearer $token",
            request = form.toDto(),
        )
        val userId = response.data?.userId
        if (response.success && !userId.isNullOrBlank()) {
            Result.success(userId)
        } else {
            Result.failure(Exception("No se pudo completar el registro del expediente."))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateExpedientStatus(recordId: String, status: String): Result<Unit> = try {
        // Si usas un AuthInterceptor en Retrofit no necesitas el Header explícito.
        // De lo contrario, puedes inyectar tu AuthRepository / TokenManager aquí.
        val response = api.updateExpedientStatus(
            recordId = recordId,
            authorization = "", // O el token obtenido de tu TokenManager/SessionManager
            request = UpdateExpedientStatusRequestDto(status = status),
        )
        if (response.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message ?: "No se pudo actualizar el estatus del expediente."))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
