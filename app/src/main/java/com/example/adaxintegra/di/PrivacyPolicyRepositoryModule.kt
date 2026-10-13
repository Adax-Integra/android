package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.PrivacyPolicyRepositoryImpl
import com.example.adaxintegra.domain.repository.PrivacyPolicyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrivacyPolicyRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPrivacyPolicyRepository(implementation: PrivacyPolicyRepositoryImpl): PrivacyPolicyRepository
}
