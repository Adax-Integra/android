package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.LocationApi
import com.example.adaxintegra.data.repository.CountryRepositoryImpl
import com.example.adaxintegra.domain.repository.CountryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

// G-07: gives the API and the repository of the countries catalog.
// It uses the same Retrofit created in NetworkModule
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit): LocationApi = retrofit.create(LocationApi::class.java)

    // when asked for CountryRepository, give CountryRepositoryImpl
    @Provides
    @Singleton
    fun provideCountryRepository(implementation: CountryRepositoryImpl): CountryRepository = implementation
}
