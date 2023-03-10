package com.waterpump.manager.api

import com.waterpump.manager.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EndpointFactory {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.PUMP_TASK_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun<T> createEndpoint(service: Class<T>):T {
        return retrofit.create(service)
    }
}