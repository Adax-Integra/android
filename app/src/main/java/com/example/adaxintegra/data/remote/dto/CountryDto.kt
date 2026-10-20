package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-07: response of GET /api/countries-states
data class CountriesResponseDto(
    val success: Boolean,
    val data: List<CountryDto>?,
)

data class CountryDto(
    @SerializedName("name_es") val nameEs: String?,
    @SerializedName("phone_code") val phoneCode: String?,
    val states: List<CountryStateDto>?,
)

data class CountryStateDto(
    @SerializedName("name_es") val nameEs: String?,
)
