package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.CaseRepositoryImpl
import com.example.adaxintegra.domain.repository.CaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// when asked for CaseRepository, give CaseRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class CaseRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCaseRepository(implementation: CaseRepositoryImpl): CaseRepository
}
