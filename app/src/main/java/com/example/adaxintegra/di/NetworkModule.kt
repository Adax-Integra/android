package com.example.adaxintegra.di

import com.example.adaxintegra.data.remote.api.CaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // TODO: replace with the real backend URL once it is deployed.
    // Local development: run "adb reverse tcp:3001 tcp:3001" before running the app
    // so localhost on the emulator/phone reaches the backend on the computer.
    // (10.0.2.2 does not work on Android 17 emulators due to local network protection)
    private const val BASE_URL = "http://localhost:3001/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCaseApi(retrofit: Retrofit): CaseApi = retrofit.create(CaseApi::class.java)
}
