package com.example.adaxintegra

import com.example.adaxintegra.data.remote.api.LocationApi
import com.example.adaxintegra.data.remote.dto.CountriesResponseDto
import com.example.adaxintegra.data.remote.dto.CountryDto
import com.example.adaxintegra.data.remote.dto.CountryStateDto
import com.example.adaxintegra.data.repository.CountryRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryRepositoryTest {

    // API de prueba: cuenta cuántas veces se pide el catálogo.
    private class FakeLocationApi(
        private val countries: List<CountryDto>,
    ) : LocationApi {
        var calls = 0

        override suspend fun getCountries(): CountriesResponseDto {
            calls++
            return CountriesResponseDto(success = true, data = countries)
        }
    }

    @Test
    fun countries_areMappedWithTheirSpanishNames() = runBlocking {
        val api = FakeLocationApi(
            listOf(
                CountryDto(
                    nameEs = "México",
                    phoneCode = "+52",
                    states = listOf(CountryStateDto(nameEs = "Querétaro"), CountryStateDto(nameEs = null)),
                ),
                // Sin nombre no se puede mostrar, se descarta
                CountryDto(nameEs = null, phoneCode = "+1", states = null),
            ),
        )

        val countries = CountryRepositoryImpl(api).getCountries()

        assertEquals(1, countries.size)
        assertEquals("México", countries[0].name)
        assertEquals(listOf("Querétaro"), countries[0].states)
    }

    @Test
    fun catalog_isDownloadedOnlyOnce() = runBlocking {
        val api = FakeLocationApi(listOf(CountryDto(nameEs = "México", phoneCode = "+52", states = emptyList())))
        val repository = CountryRepositoryImpl(api)

        repository.getCountries()
        repository.getCountries()

        assertEquals(1, api.calls)
    }

    @Test
    fun emptyCatalog_isAskedAgain() = runBlocking {
        val api = FakeLocationApi(emptyList())
        val repository = CountryRepositoryImpl(api)

        repository.getCountries()
        repository.getCountries()

        assertEquals(2, api.calls)
    }
}
