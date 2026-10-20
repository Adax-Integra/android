package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ViolenceTypesResponseDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ViolenceApi {
    // R-07: gets the catalog of all available violence types
    @GET("api/violence-types")
    suspend fun getViolenceTypes(
        @Header("Authorization") authorization: String,
    ): ViolenceTypesResponseDto
}
