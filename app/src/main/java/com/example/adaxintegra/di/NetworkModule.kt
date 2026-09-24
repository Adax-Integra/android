package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.api.CaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Network module is the newtwork manager for the app,
// it makes communications whenever someone need an internet communication
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://localhost:3001" // "https://api.adaxintegra.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCaseApi(retrofit: Retrofit): CaseApi = retrofit.create(CaseApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}
