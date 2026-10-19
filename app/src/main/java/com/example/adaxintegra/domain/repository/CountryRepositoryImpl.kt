package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.LocationApi
import com.example.adaxintegra.domain.model.Country
import com.example.adaxintegra.domain.repository.CountryRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

// G-07: the catalog almost never changes, so it is downloaded once
// and kept in memory while the app is running
@Singleton
class CountryRepositoryImpl @Inject constructor(
    private val api: LocationApi,
) : CountryRepository {
    private val mutex = Mutex()
    private var cached: List<Country>? = null

    override suspend fun getCountries(): List<Country> = mutex.withLock {
        cached?.let { return@withLock it }

        val response = api.getCountries()
        val countries = response.data.orEmpty().mapNotNull { it.toDomain() }

        // An empty answer is not saved, so the next screen asks again
        if (countries.isNotEmpty()) {
            cached = countries
        }
        countries
    }
}
