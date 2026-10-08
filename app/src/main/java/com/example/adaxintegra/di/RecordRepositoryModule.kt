package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.RecordRepositoryImpl
import com.example.adaxintegra.domain.repository.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// when asked for RecordRepository, give RecordRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class RecordRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRecordRepository(implementation: RecordRepositoryImpl): RecordRepository
}
