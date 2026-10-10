package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.data.remote.dto.CreateCaseErrorDto
import com.example.adaxintegra.domain.common.FieldValidationException
import com.example.adaxintegra.domain.entities.CasePage
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.NewCase
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.CaseRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaseRepositoryImpl
@Inject
constructor(
    private val api: CaseApi,
    private val authRepository: AuthRepository,
) : CaseRepository {

    // Reads the per-field reasons the backend sends with a rejected case (R-02)
    private val gson = Gson()

    // Reads the current token whenever a request is made
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar los casos",
            )
        }
        return "Bearer $token"
    }

    override suspend fun getCaseById(caseId: String): Case {
        val response = api.getCaseById(
            caseId = caseId,
            authorization = authorizationHeader(),
        )
        return response.data.toDomain()
    }

    override suspend fun getCases(
        search: String,
        urgency: String,
        page: Int,
        limit: Int,
    ): CasePage {
        val response = api.getCases(
            authorization = authorizationHeader(),
            search = search,
            urgency = urgency,
            page = page,
            limit = limit,
        )

        if (!response.success) {
            throw IllegalStateException("Unable to retrive cases.")
        }
        val data = response.data

        return CasePage(
            cases = data.cases.map { it.toDomain() },
            total = data.total,
            page = data.page,
            limit = data.limit,
        )
    }

    override suspend fun getCasesFromUser(userId: String): List<Case> {
        val response = api.getCasesFromUser(
            userId = userId,
            authorization = authorizationHeader(),
        )
        return response.data.map { it.toDomain() }
    }

    override suspend fun getExternalUserCases(userId: String): List<Case> {
        val response = api.getExternalUserCases(
            userId = userId,
            authorization = authorizationHeader(),
        )
        return response.data.map { it.toDomain() }
    }

    // R-02: registers the case the external user filled in the form
    override suspend fun createCase(userId: String, newCase: NewCase): Case {
        val response = try {
            api.createCase(
                userId = userId,
                authorization = authorizationHeader(),
                request = newCase.toRequestDto(),
            )
        } catch (e: HttpException) {
            throw fieldValidationException(e) ?: e
        }

        val created = response.data

        if (!response.success || created == null) {
            throw IllegalStateException("Unable to create the case.")
        }

        return created.toDomain()
    }

    // Reads { success, error, errors: { campo: motivo } } out of a 400 answer
    private fun fieldValidationException(e: HttpException): FieldValidationException? {
        // 400 is the only status that carries one reason per field
        if (e.code() != 400) {
            return null
        }

        val body = e.response()?.errorBody()?.string()

        if (body.isNullOrBlank()) {
            return null
        }

        val parsed = try {
            gson.fromJson(body, CreateCaseErrorDto::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }

        val fieldErrors = parsed?.errors?.takeIf { it.isNotEmpty() } ?: return null

        return FieldValidationException(
            fieldErrors = fieldErrors,
            message = parsed.error ?: "Invalid case data.",
        )
    }
}
