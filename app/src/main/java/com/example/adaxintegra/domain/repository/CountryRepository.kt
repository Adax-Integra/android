package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Country

interface CountryRepository {
    // G-07: countries with their states, in the order the backend sends them
    suspend fun getCountries(): List<Country>
}
