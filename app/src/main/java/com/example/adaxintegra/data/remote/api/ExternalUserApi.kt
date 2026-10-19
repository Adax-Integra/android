package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ExternalUserListResponseDto
import com.example.adaxintegra.data.remote.dto.ExternalProfileResponseDto
import com.example.adaxintegra.data.remote.dto.UpdateExternalProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExternalUserApi {
    // G-16: admin consults the list of external accounts (requires the admin token).
    // A null search is not sent, so the backend does not filter by text
    @GET("api/internal-users/external-users")
    suspend fun getExternalUsers(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("status") status: String,
        @Query("sort") sort: String,
    ): ExternalUserListResponseDto

    // G-07: retrieve an external user's profile
    @GET("api/internal-users/external-users/{userId}")
    suspend fun getExternalProfile(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
    ): ExternalProfileResponseDto

    // G-07: update editable fields with administrator authorization
    @PATCH("api/internal-users/external-users/{userId}")
    suspend fun updateExternalProfile(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body request: UpdateExternalProfileRequestDto,
    ): ExternalProfileResponseDto
}
