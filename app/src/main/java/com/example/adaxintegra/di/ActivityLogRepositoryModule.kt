package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.ActivityLogRepositoryImpl
import com.example.adaxintegra.domain.repository.ActivityLogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// when asked for ActivityLogRepository, give ActivityLogRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityLogRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindActivityLogRepository(implementation: ActivityLogRepositoryImpl): ActivityLogRepository
}
