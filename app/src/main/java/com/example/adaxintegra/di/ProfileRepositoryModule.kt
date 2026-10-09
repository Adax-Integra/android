package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.ProfileRepositoryImpl
import com.example.adaxintegra.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(implementation: ProfileRepositoryImpl): ProfileRepository
}
