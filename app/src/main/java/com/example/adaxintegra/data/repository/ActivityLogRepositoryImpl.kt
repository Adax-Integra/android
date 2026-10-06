package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.ActivityLogApi
import com.example.adaxintegra.domain.model.ActivityLogPage
import com.example.adaxintegra.domain.repository.ActivityLogRepository
import com.example.adaxintegra.domain.repository.AuthRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

// V-06: reads the activity log from the backend
@Singleton
class ActivityLogRepositoryImpl @Inject constructor(
    private val api: ActivityLogApi,
    private val authRepository: AuthRepository,
) : ActivityLogRepository {
    // Reads the current token from the login session
    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para consultar la bitácora")
        }
        return "Bearer $token"
    }

    // The backend expects dates in UTC with time zone (ex. 2026-10-04T06:00:00Z)
    private fun toUtcText(date: Date?): String? {
        if (date == null) return null
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    }

    override suspend fun getActivityLog(page: Int, from: Date?, to: Date?): ActivityLogPage {
        val response = api.getActivityLog(
            authorization = authorizationHeader(),
            page = page,
            from = toUtcText(from),
            to = toUtcText(to),
        )
        val data = response.data ?: throw IllegalStateException("Empty response from server")
        return data.toDomain()
    }
}
