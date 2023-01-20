package com.waterpump.manager.api

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiServiceTest {

    @Captor
    private lateinit var callbackCaptor: ArgumentCaptor<Callback<Tasks>>

    @Mock
    private lateinit var endpointFactory:EndpointFactory

    private lateinit var instance:ApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        instance = ApiService(endpointFactory)
    }

    @Test
    fun `Should return list of tasks when fetching pending tasks`() {
        val taskCall:Call<Tasks> = mockEndpointGetPendingTasks()
        val tasks = Tasks(listOf(Task(1, 0.0f, false)))
        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, Response.success(tasks))
        }

        val fetchPendingTasks = instance.fetchPendingTasks()

        assertThat(fetchPendingTasks).hasSize(1)
    }

    @Test
    fun `Should throw an exception when returning pending tasks with body is not present`() {
        val taskCall: Call<Tasks> = mockEndpointGetPendingTasks()

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, Response.success(null))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThat(exception)
            .isNotNull

        assertThat(exception!!.message)
            .isNotNull
            .isEqualTo("Null body was returned by the GET tasks endpoint")
    }

    @Test
    fun `Should throw an exception when fail to get a response from endpoint`() {
        val taskCall: Call<Tasks> = mockEndpointGetPendingTasks()
        val errorMessage = "network error"

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onFailure(taskCall, java.lang.RuntimeException(errorMessage))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThat(exception)
            .isNotNull

        assertThat(exception!!.message)
            .isNotNull
            .isEqualTo(errorMessage)
    }

    @Test
    fun `Should set tasks as concluded when endpoint is called`() {
        val taskEndpoint = mock(TaskEndpoints::class.java)
        val taskCall: Call<Task> = mock(Call::class.java) as Call<Task>
        val pendingTask = Task(1, 1.0f, true)
        val finishedTask = Task(1, 1.0f, false)

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java))
            .thenReturn(taskEndpoint)
        Mockito.`when`(taskEndpoint.postPendingTasks(pendingTask)).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Task>).onResponse(taskCall, Response.success(finishedTask))
        }

        val responseTask = instance.markTasksAsConcluded(pendingTask)

        verify(taskCall).enqueue(any())
        assertThat(responseTask.pending).isFalse
    }

    private fun mockEndpointGetPendingTasks(): Call<Tasks> {
        val taskEndpoint = mock(TaskEndpoints::class.java)
        val taskCall: Call<Tasks> = mock(Call::class.java) as Call<Tasks>

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java))
            .thenReturn(taskEndpoint)
        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)
        return taskCall
    }
}