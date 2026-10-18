package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.RecordDocumentsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object V13Module {

    // Same as R-06: signed URLs and identity documents must never reach Logcat
    @Provides
    @Singleton
    fun provideRecordDocumentsApi(
        retrofit: Retrofit,
        okHttpClient: OkHttpClient,
    ): RecordDocumentsApi {
        val documentClient = okHttpClient.newBuilder()
            .apply {
                interceptors().removeAll {
                    it is HttpLoggingInterceptor
                }
                networkInterceptors().removeAll {
                    it is HttpLoggingInterceptor
                }
            }
            .build()

        val documentRetrofit = retrofit.newBuilder()
            .client(documentClient)
            .build()

        return documentRetrofit.create(
            RecordDocumentsApi::class.java,
        )
    }
}
