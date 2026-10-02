package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.CollaboratorRepositoryImpl
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// when asked for CollaboratorRepository, give CollaboratorRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class CollaboratorRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCollaboratorRepository(implementation: CollaboratorRepositoryImpl) : CollaboratorRepository
}
