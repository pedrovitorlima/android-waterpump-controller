package com.waterpump.manager.api

import com.waterpump.manager.ui.main.LogViewerWrapper

class ApiService(
    private val endpointFactory: EndpointFactory,
) {

    fun fetchPendingTasks(): List<Task?>? {
        val pendingTaskEndpoint =
            endpointFactory.createEndpoint(TaskEndpoints::class.java)

        val execute = pendingTaskEndpoint.getPendingTasks().execute()
        val tasks = execute.body()

        if (tasks != null) {
            return tasks.result
        }

        return null
    }
}