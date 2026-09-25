package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login") // adjust this route to the endpoint
    suspend fun login(
        @Body request: LoginRequestDto,
    ): LoginResponseDto
}
