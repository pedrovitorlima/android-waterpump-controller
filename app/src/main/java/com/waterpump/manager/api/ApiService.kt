package com.waterpump.manager.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService(
    private val endpointFactory: EndpointFactory,
) {
    private var taskEndpoints: TaskEndpoints

    init {
        taskEndpoints = endpointFactory.createEndpoint(TaskEndpoints::class.java)
    }

    fun fetchPendingTasks(): List<Task> {
        val call = taskEndpoints.getPendingTasks()

        var tasks:Tasks = Tasks(emptyList())
        call.enqueue(object: Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {
                tasks = response.body() ?: throw java.lang.RuntimeException("Null body was returned by the GET tasks endpoint")
            }

            override fun onFailure(call: Call<Tasks>, t: Throwable) {
                throw t
            }
        })

        return tasks.result
    }

    fun markTasksAsConcluded(task: Task): Task {
        var responseTask:Task = Task(0, 0f, false)
        val call = taskEndpoints.postPendingTasks(task)

        call.enqueue(object: Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                responseTask = response.body() ?: throw java.lang.RuntimeException("Null body was returned by the POST tasks endpoint")
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                throw t
            }
        })

        return responseTask
    }
}