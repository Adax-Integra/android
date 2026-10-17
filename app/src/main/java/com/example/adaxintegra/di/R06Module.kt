package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// R-06: the backend answers after saving the photo in Storage,
// the 10 s that OkHttp uses by default are not enough on a slow network
private const val DOCUMENT_TIMEOUT_SECONDS = 20L

@Module
@InstallIn(SingletonComponent::class)
object R06Module {

    @Provides
    @Singleton
    fun provideUploadDocumentsApi(
        retrofit: Retrofit,
        okHttpClient: OkHttpClient,
    ): UploadDocumentsApi {
        val documentClient = okHttpClient.newBuilder()
            .apply {
                interceptors().removeAll {
                    it is HttpLoggingInterceptor
                }
                networkInterceptors().removeAll {
                    it is HttpLoggingInterceptor
                }
            }
            .writeTimeout(DOCUMENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DOCUMENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        val documentRetrofit = retrofit.newBuilder()
            .client(documentClient)
            .build()

        return documentRetrofit.create(
            UploadDocumentsApi::class.java,
        )
    }
}
