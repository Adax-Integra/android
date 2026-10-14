package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CaseDetailResponseDto
import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import com.example.adaxintegra.data.remote.dto.CaseResponseDto
import com.example.adaxintegra.data.remote.dto.CloseCaseResponseDto
import com.example.adaxintegra.data.remote.dto.CreateCaseRequestDto
import com.example.adaxintegra.data.remote.dto.CreateCaseResponseDto
import com.example.adaxintegra.data.remote.dto.UserCasesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

// retrieve backend data which will come in as a JSON
interface CaseApi {
    @GET("api/cases/{caseId}")
    suspend fun getCaseById(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
    ): CaseResponseDto

    // V-11: retrieves the complete information required by the case detail screen
    @GET("api/cases/{caseId}")
    suspend fun getCaseDetail(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
    ): CaseDetailResponseDto

    // V-11: closes the selected case
    @PATCH("api/cases/{caseId}/close")
    suspend fun closeCase(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
    ): CloseCaseResponseDto

    @GET("api/cases")
    suspend fun getCases(
        @Header("Authorization") authorization: String,
        @Query("search") search: String = "",
        @Query("urgency") urgency: String = "",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): CaseListResponseDto

    @GET("api/internal-users/{userId}/allCases")
    suspend fun getCasesFromUser(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
    ): UserCasesResponseDto

    // V-04: cases of the external user
    @GET("api/users/{userId}/cases")
    suspend fun getExternalUserCases(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
    ): UserCasesResponseDto

    //R-02: the external user registers a new case
    @POST("api/external-users/{userId}/cases")
    suspend fun createCase(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Body request: CreateCaseRequestDto,
    ): CreateCaseResponseDto
}
