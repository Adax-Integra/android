package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import com.example.adaxintegra.data.remote.dto.CaseResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

// retrieve backend data which will come in as a JSON
interface CaseApi {
    @GET("api/cases/{caseId}")
    suspend fun getCaseById(
        @Path("caseId") caseId: String,
    ): CaseResponseDto

    @GET("api/internal-users/{userId}/allCases")
    suspend fun getCasesFromUser(
        @Path("userId") userId: String,
    ): CaseListResponseDto
}
