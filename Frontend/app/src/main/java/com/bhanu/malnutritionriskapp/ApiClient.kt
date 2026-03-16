package com.bhanu.malnutritionriskapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL =
        "https://poshanrisk-backend-1.onrender.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    com.google.gson.GsonBuilder()
                        .serializeNulls()
                        .create()
                )
            )
            .build()
            .create(ApiService::class.java)
    }
}
