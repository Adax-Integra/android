package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.ExpedientRepositoryImpl
import com.example.adaxintegra.domain.repository.ExpedientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExpedientRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExpedientRepository(
        expedientRepositoryImpl: ExpedientRepositoryImpl,
    ): ExpedientRepository
}
