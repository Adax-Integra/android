package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.DeactivateAccountResponseDto
import com.example.adaxintegra.data.remote.dto.ProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

//simply calling for userId to find correct profile to display
interface ProfileApi {
    @GET("api/profile/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): ProfileResponseDto

    // G-08: the user deactivates her own account, the id must match the token
    @PATCH("api/profile/{userId}/deactivate")
    suspend fun deactivateAccount(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): DeactivateAccountResponseDto
}
