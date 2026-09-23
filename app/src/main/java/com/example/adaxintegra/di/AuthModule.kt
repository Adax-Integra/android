package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.AuthRepositoryImpl
import com.example.adaxintegra.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// in this file we are telling Hilt when someone asks for an AuthRepoitory
// it has to return one AuthRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository
}
