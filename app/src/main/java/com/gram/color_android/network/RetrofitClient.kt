package com.gram.color_android.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val SPRING_BOOT_BASE_URL = "http://211.38.86.92:8009"
        private const val FAST_API_BASE_URL = "http://13.209.8.210:5000"
    private var retrofit_spring : Retrofit
    private var retrofit_fast_api : Retrofit
    private var colorSpringAPI : ColorAPI
    private var colorFastAPI : ColorAPI

    init {
        val loggingInterceptor =  HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(TokenAuthenticator())
            .build()
        retrofit_spring = Retrofit.Builder()
            .baseUrl(SPRING_BOOT_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit_fast_api = Retrofit.Builder()
            .baseUrl(FAST_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        colorSpringAPI = retrofit_spring.create(ColorAPI::class.java)
        colorFastAPI = retrofit_fast_api.create(ColorAPI::class.java)
    }

    fun getSpringAPI() = colorSpringAPI
    fun getFastAPI() = colorFastAPI
}