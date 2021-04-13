package com.kode.recipes.di.base

import com.kode.recipes.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://test.kode-t.ru/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Парсинг JSON
        .build()

    // JSON парсер с доп. адаптером для kotlin
    private val moshi: Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // Клиент okhttp
    private val client: OkHttpClient
        get() {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(loggingInterceptor)
            }
            return okHttpClientBuilder.build()
        }
}