package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ChangePasswordRequestDto
import com.example.adaxintegra.data.remote.dto.ChangePasswordResponseDto
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.LoginResponseDto
import com.example.adaxintegra.data.remote.dto.MessageResponseDto
import com.example.adaxintegra.data.remote.dto.RegisterRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterResponseDto
import com.example.adaxintegra.data.remote.dto.ResendVerificationRequestDto
import com.example.adaxintegra.data.remote.dto.VerifyEmailRequestDto

import retrofit2.http.Body
import retrofit2.http.Header
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

    // G-09 (verify the email woth the 6 digit code; returns the session like login)
    @POST("api/auth/verify-email")
    suspend fun verifyEmail(
        @Body request: VerifyEmailRequestDto,
    ): LoginResponseDto

    // G-09 POST (send a new verification code)
    @POST("api/auth/resend-verification")
    suspend fun resendVerification(
        @Body request: ResendVerificationRequestDto,
    ): MessageResponseDto
    
    // POST (change password)
    @POST("api/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body request: ChangePasswordRequestDto,
    ): ChangePasswordResponseDto
}
