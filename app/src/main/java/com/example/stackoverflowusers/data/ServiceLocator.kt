package com.example.stackoverflowusers.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {

    private const val BASE_URL = "https://api.stackexchange.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: StackExchangeApi by lazy { retrofit.create(StackExchangeApi::class.java) }

    val userRepository: UserRepository by lazy { UserRepository(api) }
}
