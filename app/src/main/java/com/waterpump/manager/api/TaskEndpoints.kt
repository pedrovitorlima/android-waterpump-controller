package com.waterpump.manager.api

import com.waterpump.manager.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface TaskEndpoints {
//    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("task/pending")
    fun getPendingTasks(): Call<Tasks>
}

data class Tasks (
    val result: List<Task>
)

data class Task (
    val id: Int,
    val seconds: Float
)

