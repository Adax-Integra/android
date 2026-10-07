package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ForgotPasswordRequestDto
import com.example.adaxintegra.data.remote.dto.ForgotPasswordResponseDto
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.LoginResponseDto
import com.example.adaxintegra.data.remote.dto.RegisterRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterResponseDto
import com.example.adaxintegra.data.remote.dto.ResetPasswordRequestDto
import com.example.adaxintegra.data.remote.dto.ResetPasswordResponseDto

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // R-01 POST (login)
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): LoginResponseDto

    // G-01 POST (register an account)
    @POST("api/external-user/register")
    suspend fun register(
        @Body request: RegisterRequestDto,
    ): RegisterResponseDto

    @POST("api/password-recovery/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto,
    ): ForgotPasswordResponseDto

    @POST("api/password-recovery/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequestDto,
    ): ResetPasswordResponseDto
}
