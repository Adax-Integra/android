
package com.example.adaxintegra.di

import com.example.adaxintegra.data.repository.UploadDocumentsRepositoryImpl
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UploadDocumentsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUploadDocumentsRepository(
        implementation: UploadDocumentsRepositoryImpl,
    ): UploadDocumentsRepository
}
