package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import com.example.adaxintegra.data.remote.dto.CaseResponseDto
import com.example.adaxintegra.data.remote.dto.UserCasesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// retrieve backend data which will come in as a JSON
interface CaseApi {
    @GET("api/cases/{caseId}")
    suspend fun getCaseById(
        @Path("caseId") caseId: String,
    ): CaseResponseDto

    @GET("api/cases")
    suspend fun getCases(
        @Query("search") search: String = "",
        @Query("urgency") urgency: String = "",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): CaseListResponseDto

    @GET("api/internal-users/{userId}/allCases")
    suspend fun getCasesFromUser(
        @Path("userId") userId: String,
    ): UserCasesResponseDto

    // V-04: cases of the external user
    @GET("api/users/{userId}/cases")
    suspend fun getExternalUserCases(
        @Path("userId") userId: String,
    ): UserCasesResponseDto
}
