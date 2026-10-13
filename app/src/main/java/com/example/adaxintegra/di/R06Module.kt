
package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object R06Module {

    @Provides
    @Singleton
    fun provideUploadDocumentsApi(retrofit: Retrofit): UploadDocumentsApi {
        return retrofit.create(UploadDocumentsApi::class.java)
    }
}
