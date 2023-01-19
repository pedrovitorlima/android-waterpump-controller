package com.waterpump.manager.api

class ApiService(
    private val endpointFactory: EndpointFactory,
) {

    fun fetchPendingTasks(): List<Task> {
        val pendingTaskEndpoint =
            endpointFactory.createEndpoint(TaskEndpoints::class.java)

        val execute = pendingTaskEndpoint.getPendingTasks().execute()
        val tasks = execute.body()
            ?: throw java.lang.RuntimeException("Null body was returned by the tasks endpoint")

        return tasks.result
    }
}