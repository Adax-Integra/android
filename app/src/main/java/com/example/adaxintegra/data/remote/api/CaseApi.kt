package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import com.example.adaxintegra.data.remote.dto.CaseResponseDto
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
}
