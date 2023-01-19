package com.waterpump.manager.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService(
    private val endpointFactory: EndpointFactory,
) {

    fun fetchPendingTasks(): List<Task> {
        val pendingTaskEndpoint =
            endpointFactory.createEndpoint(TaskEndpoints::class.java)

        val call = pendingTaskEndpoint.getPendingTasks()

        var tasks:Tasks = Tasks(emptyList())
        call.enqueue(object: Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {
                tasks = response.body() ?: throw java.lang.RuntimeException("Null body was returned by the tasks endpoint")
            }

            override fun onFailure(call: Call<Tasks>, t: Throwable) {
                throw t
            }
        })

        return tasks.result
    }
}