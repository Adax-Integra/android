package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.AcceptConsentRequestDto
import com.example.adaxintegra.data.remote.dto.AcceptConsentResponseDto
import com.example.adaxintegra.data.remote.dto.CurrentPrivacyPolicyResponseDto
import com.example.adaxintegra.data.remote.dto.PrivacyConsentResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PrivacyPolicyApi {

    @GET("api/privacy-policy/current")
    suspend fun getCurrentPrivacyPolicy(
        @Header("Authorization") authorization: String,
    ): CurrentPrivacyPolicyResponseDto

    @GET("api/privacy-policy/consent")
    suspend fun getPrivacyConsent(
        @Header("Authorization") authorization: String,
    ): PrivacyConsentResponseDto

    @POST("api/privacy-policy/consent")
    suspend fun acceptPrivacyPolicy(
        @Header("Authorization") authorization: String,
        @Body request: AcceptConsentRequestDto,
    ): AcceptConsentResponseDto
}
