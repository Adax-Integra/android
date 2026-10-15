package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.EventRepositoryImpl
import com.example.adaxintegra.domain.repository.EventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl,
    ): EventRepository
}
