package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.Country
import com.example.adaxintegra.domain.repository.CountryRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

// G-07: the catalog is only needed to edit the address,
// so a failure returns an empty list instead of blocking the detail
class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository,
) {
    suspend operator fun invoke(): List<Country> = try {
        repository.getCountries()
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        emptyList()
    }
}
