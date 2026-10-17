package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDto
import com.example.adaxintegra.data.remote.api.ExpedientApi
import com.example.adaxintegra.data.remote.api.RecordApi
import com.example.adaxintegra.data.remote.dto.UpdateExpedientStatusRequestDto
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.ExpedientRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class ExpedientRepositoryImpl @Inject constructor(
    private val expedientApi: ExpedientApi,
    private val recordApi: RecordApi,
    private val authRepository: AuthRepository,
) : ExpedientRepository {

    private suspend fun authorizationHeader(): String {
        val session = authRepository.session.filterNotNull().first()
        val token = session.token
        if (token.isBlank()) {
            throw IllegalStateException("Sesión no válida")
        }
        return "Bearer $token"
    }

    override suspend fun registerExpedient(token: String, form: PersonalDataForm): Result<Unit> = try {
        val response = expedientApi.registerExpedient(
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

    override suspend fun updateExpedientStatus(recordId: String, status: String): Result<Unit> {
        return try {
            val response = recordApi.updateRecordStatus(
                recordId = recordId,
                authorization = authorizationHeader(),
                body = UpdateExpedientStatusRequestDto(status = status)
            )
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Error al actualizar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        }
}
