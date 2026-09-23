package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CaseListApi {
    @GET("api/internal-users/{userId}/allCases")
    suspend fun getCasesFromUser(
        @Path("userId") userId: String,
    ): CaseListResponseDto
}
