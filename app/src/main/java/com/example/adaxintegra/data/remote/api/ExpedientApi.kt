package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RegisterExpedientRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterExpedientResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ExpedientApi {
    @POST("api/internal-users/register-external")
    suspend fun registerExpedient(
        @Header("Authorization") authorization: String,
        @Body request: RegisterExpedientRequestDto,
    ): RegisterExpedientResponseDto
}
