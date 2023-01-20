package com.waterpump.manager.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskEndpoints {
//    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("task/pending")
    fun getPendingTasks(): Call<Tasks>

    @POST
    fun postPendingTasks(@Body task:Task): Call<Task>
}

data class Tasks (
    val result: List<Task>
)

data class Task (
    val id: Int,
    val seconds: Float,
    val pending: Boolean
)

