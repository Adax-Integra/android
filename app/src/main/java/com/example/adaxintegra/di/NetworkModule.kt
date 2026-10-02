package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.api.CaseApi
import com.example.adaxintegra.data.remote.api.CollaboratorApi
import com.example.adaxintegra.data.remote.api.ExpedientApi
import com.example.adaxintegra.data.remote.api.ProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Network module is the network manager for the app,
// it makes communications whenever someone needs an internet communication
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://adax-integra.duckdns.org"

    // Local testing: use "http://localhost:3001/", enable networkSecurityConfig
    // in AndroidManifest and run "adb reverse tcp:3001 tcp:3001".
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Avoids logging credentials, tokens and case information
            level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCaseApi(retrofit: Retrofit): CaseApi = retrofit.create(CaseApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCollaboratorApi(retrofit: Retrofit): CollaboratorApi = retrofit.create(CollaboratorApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideExpedientApi(retrofit: Retrofit): ExpedientApi = retrofit.create(ExpedientApi::class.java)
}
