package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RegisterExpedientRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterExpedientResponseDto
import com.example.adaxintegra.data.remote.dto.UpdateExpedientStatusRequestDto
import com.example.adaxintegra.data.remote.dto.UpdateExpedientStatusResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpedientApi {
    @POST("api/internal-users/external-users")
    suspend fun registerExpedient(
        @Header("Authorization") authorization: String,
        @Body request: RegisterExpedientRequestDto,
    ): RegisterExpedientResponseDto

    @PATCH("api/records/{recordId}/status")
    suspend fun updateRecordStatus(
        @Path("recordId") recordId: String,
        @Header("Authorization") authorization: String,
        @Body request: UpdateExpedientStatusRequestDto,
    ): UpdateExpedientStatusResponseDto
}
