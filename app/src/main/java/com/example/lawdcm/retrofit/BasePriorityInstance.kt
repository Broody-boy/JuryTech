package com.example.lawdcm.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BasePriorityInstance {
    private var retrofitInstance : Retrofit? = null
    private val BASE_URL : String = "http://ec2-13-51-150-26.eu-north-1.compute.amazonaws.com:8080"

    fun getBasePriority() : Retrofit {
        if(retrofitInstance == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofitInstance!!
        }
        return retrofitInstance!!
    }
}