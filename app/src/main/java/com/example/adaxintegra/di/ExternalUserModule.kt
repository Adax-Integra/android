package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.ExternalUserApi
import com.example.adaxintegra.data.repository.ExternalUserRepositoryImpl
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

// G-16: gives the API and the repository of the external users list.
// It uses the same Retrofit created in NetworkModule
@Module
@InstallIn(SingletonComponent::class)
object ExternalUserModule {
    @Provides
    @Singleton
    fun provideExternalUserApi(retrofit: Retrofit): ExternalUserApi =
        retrofit.create(ExternalUserApi::class.java)

    // when asked for ExternalUserRepository, give ExternalUserRepositoryImpl
    @Provides
    @Singleton
    fun provideExternalUserRepository(implementation: ExternalUserRepositoryImpl): ExternalUserRepository =
        implementation
}
