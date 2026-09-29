package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RegisterExpedientRequestDto
import retrofit2.http.GET
import retrofit2.http.Path

//simply calling for userId to find correct profile to display
interface ProfileApi {
    @GET("api/profile/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
    ): RegisterExpedientRequestDto
}
