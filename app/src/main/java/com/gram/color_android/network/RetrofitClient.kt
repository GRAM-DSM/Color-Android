package com.gram.color_android.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val SPRING_BOOT_BASE_URL = "http://211.38.86.92:8009"
    private var retrofit_spring : Retrofit
    private var colorAPI : ColorAPI

    init {
        val loggingInterceptor =  HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(TokenAuthenticator())
            .build()
        retrofit_spring = Retrofit.Builder()
            .baseUrl(SPRING_BOOT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        colorAPI = retrofit_spring.create(ColorAPI::class.java)
    }

    fun getAPI() = colorAPI
}