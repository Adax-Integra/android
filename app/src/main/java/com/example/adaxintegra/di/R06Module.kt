
package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

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
            .build()

        val documentRetrofit = retrofit.newBuilder()
            .client(documentClient)
            .build()

        return documentRetrofit.create(
            UploadDocumentsApi::class.java,
        )
    }
}
