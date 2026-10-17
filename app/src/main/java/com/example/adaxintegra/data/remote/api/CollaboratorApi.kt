package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CollaboratorDetailResponseDto
import com.example.adaxintegra.data.remote.dto.CollaboratorListResponseDto
import com.example.adaxintegra.data.remote.dto.CreateCollaboratorRequestDto
import com.example.adaxintegra.data.remote.dto.CreateCollaboratorResponseDto
import com.example.adaxintegra.data.remote.dto.UpdateCollaboratorStatusRequestDto
import com.example.adaxintegra.data.remote.dto.UpdateCollaboratorStatusResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CollaboratorApi {
    // G-03 : admin creates a collaborator account (requires the admin token)
    @POST("api/internal-users")
    suspend fun createCollaborator(
        @Header("Authorization") authorization: String,
        @Body request: CreateCollaboratorRequestDto,
    ): CreateCollaboratorResponseDto

    // G-06: admin consults the list of collaborators (requires the admin token)
    @GET("api/internal-users")
    suspend fun getCollaborators(
        @Header("Authorization") authorization: String,
    ): CollaboratorListResponseDto

    // G-10: admin consults the detail of one collaborator (requires the admin token)
    @GET("api/internal-users/{userId}")
    suspend fun getCollaboratorDetail(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
    ): CollaboratorDetailResponseDto

    // G-04: admin activates or deactivates a collaborator account (requires the admin token)
    @PATCH("api/internal-users/{userId}/status")
    suspend fun updateCollaboratorStatus(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body request: UpdateCollaboratorStatusRequestDto,
    ): UpdateCollaboratorStatusResponseDto
}
