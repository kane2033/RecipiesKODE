package com.kode.recipes.data.base

import com.kode.recipes.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Объект, служащий для создания экземпляра класса ретрофит
 * */
object ApiFactory {

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

    // Создание клиента ретрофита
    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://test.kode-t.ru/")
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Парсинг JSON
        .build()

}