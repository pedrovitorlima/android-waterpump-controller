package com.waterpump.manager.api

import android.accounts.NetworkErrorException
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Call
import retrofit2.Callback


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
        val taskCall:Call<Tasks> = mockEndpointCallTask()
        val tasks = Tasks(listOf(Task(1, 0.0f)))
        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, retrofit2.Response.success(tasks))
        }

        val fetchPendingTasks = instance.fetchPendingTasks()

        assertThat(fetchPendingTasks).hasSize(1)
    }

    @Test
    fun `Should throw an exception when returning pending tasks with body is not present`() {
        val taskCall: Call<Tasks> = mockEndpointCallTask()

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, retrofit2.Response.success(null))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThat(exception)
            .isNotNull()

        assertThat(exception!!.message)
            .isNotNull()
            .isEqualTo("Null body was returned by the tasks endpoint")
    }

    @Test
    fun `Should throw an exception when fail to get a response from endpoint`() {
        val taskCall: Call<Tasks> = mockEndpointCallTask()
        val errorMessage = "network error"

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onFailure(taskCall, java.lang.RuntimeException(errorMessage))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThat(exception)
            .isNotNull()

        assertThat(exception!!.message)
            .isNotNull()
            .isEqualTo(errorMessage)
    }

    private fun mockEndpointCallTask(): Call<Tasks> {
        val taskEndpoint = mock(TaskEndpoints::class.java)
        val taskCall: Call<Tasks> = mock(Call::class.java) as Call<Tasks>
        val tasks = Tasks(listOf(Task(1, 0.0f)))

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java))
            .thenReturn(taskEndpoint)
        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)
        return taskCall
    }
}