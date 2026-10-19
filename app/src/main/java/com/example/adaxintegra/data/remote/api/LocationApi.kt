package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CountriesResponseDto
import retrofit2.http.GET

interface LocationApi {
    // G-07: catalog of countries with their states for the address dropdowns
    // The route is public, so it does not need the session token
    @GET("api/countries-states")
    suspend fun getCountries(): CountriesResponseDto
}
