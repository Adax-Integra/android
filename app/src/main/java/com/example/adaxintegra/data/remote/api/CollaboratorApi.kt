package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CreateCollaboratorRequestDto
import com.example.adaxintegra.data.remote.dto.CreateCollaboratorResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CollaboratorApi {
    // G-03 : admin creates a collaborator account (requires the admin token)
    @POST("api/internal-users")
    suspend fun createCollaborator(
        @Header("Authorization") authorization: String,
        @Body request: CreateCollaboratorRequestDto,
    ): CreateCollaboratorResponseDto
}
