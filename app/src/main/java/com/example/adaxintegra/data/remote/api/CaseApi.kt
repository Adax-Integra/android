package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.AssignCollaboratorsRequestDto
import com.example.adaxintegra.data.remote.dto.AssignableCaseCollaboratorsResponseDto
import com.example.adaxintegra.data.remote.dto.AssignHelpRequestDto
import com.example.adaxintegra.data.remote.dto.AssignHelpResponseDto
import com.example.adaxintegra.data.remote.dto.AssignViolenceRequestDto
import com.example.adaxintegra.data.remote.dto.AssignViolenceResponseDto
import com.example.adaxintegra.data.remote.dto.CaseDetailResponseDto
import com.example.adaxintegra.data.remote.dto.CaseListResponseDto
import com.example.adaxintegra.data.remote.dto.CaseResponseDto
import com.example.adaxintegra.data.remote.dto.CloseCaseResponseDto
import com.example.adaxintegra.data.remote.dto.CreateCaseRequestDto
import com.example.adaxintegra.data.remote.dto.CreateCaseResponseDto
import com.example.adaxintegra.data.remote.dto.UpdateCaseStepRequestDto
import com.example.adaxintegra.data.remote.dto.UpdateCaseStepResponseDto
import com.example.adaxintegra.data.remote.dto.UserCasesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    // R-11: Creates a case for an external user by an internal/admin user
    @POST("api/internal-users/external-users/{userId}/cases")
    suspend fun createCaseForExternalUser(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Body request: CreateCaseRequestDto,
    ): CreateCaseResponseDto

    // R-08: Assign helps to a case.
    @PUT("api/cases/{caseId}/helps")
    suspend fun assignHelpsToCase(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
        @Body request: AssignHelpRequestDto,
    ): AssignHelpResponseDto

    // R-07: Assign violences to a case.
    @PATCH("api/cases/{caseId}/violence-types")
    suspend fun assignViolenceToCase(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
        @Body request: AssignViolenceRequestDto,
    ): AssignViolenceResponseDto

    // V-12: cases assigned to the logged in collaborator (user comes from the token)
    @GET("api/cases/assigned")
    suspend fun getMyCases(
        @Header("Authorization") authorization: String,
        @Query("status") status: String = "all",
        @Query("search") search: String = "",
        @Query("urgency") urgency: String = "",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): CaseListResponseDto

    @PUT("api/cases/{caseId}/assignments")
    suspend fun replaceCaseCollaborators(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
        @Body request: AssignCollaboratorsRequestDto,
    ): Unit

    @GET("api/cases/collaborators")
    suspend fun getAssignableCaseCollaborators(
        @Header("Authorization") authorization: String,
    ): AssignableCaseCollaboratorsResponseDto
    //r-13 update case steps
    @PATCH("api/cases/{caseId}/step")
    suspend fun updateCaseStep(
        @Path("caseId") caseId: String,
        @Header("Authorization") authorization: String,
        @Body request: UpdateCaseStepRequestDto,
    ): UpdateCaseStepResponseDto
}
