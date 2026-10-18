package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ExternalUserListResponseDto
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
}
