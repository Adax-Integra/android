package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.HelpTypesResponseDto
import retrofit2.http.GET
import retrofit2.http.Header

interface HelpApi {
    // R-08: gets the catalog of all available help types
    @GET("api/help-types")
    suspend fun getHelpTypes(
        @Header("Authorization") authorization: String,
    ): HelpTypesResponseDto
}
