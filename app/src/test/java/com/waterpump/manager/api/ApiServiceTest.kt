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

    @Mock
    private lateinit var endpointFactory:EndpointFactory

    @Mock
    private lateinit var taskEndpoint:TaskEndpoints

    @Mock
    private lateinit var taskCall: Call<Tasks>

    private lateinit var instance:ApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java))
            .thenReturn(taskEndpoint)

        instance = ApiService(endpointFactory)
    }

    @Test
    fun `Should return list of tasks when fetching pending tasks`() {
        val tasks = Tasks(listOf(Task(1, 0.0f, false)))

        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, Response.success(tasks))
        }

        val fetchPendingTasks = instance.fetchPendingTasks()

        assertThat(fetchPendingTasks).hasSize(1)
    }

    @Test
    fun `Should throw an exception when returning pending tasks with body is not present`() {
        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, Response.success(null))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThatIsAsExpected(exception, "Null body was returned by the GET tasks endpoint")
    }

    @Test
    fun `Should throw an exception when fail to get a response from endpoint`() {
        val errorMessage = "network error"

        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onFailure(taskCall, java.lang.RuntimeException(errorMessage))
        }

        val exception = kotlin.runCatching {
            instance.fetchPendingTasks()
        }.exceptionOrNull()

        assertThatIsAsExpected(exception, errorMessage)
    }

    @Test
    fun `Should set tasks as concluded when endpoint is called`() {
        val taskCall: Call<Task> = mock(Call::class.java) as Call<Task>
        val pendingTask = Task(1, 1.0f, true)
        val finishedTask = Task(1, 1.0f, false)

        Mockito.`when`(taskEndpoint.postPendingTasks(pendingTask)).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Task>).onResponse(taskCall, Response.success(finishedTask))
        }

        val responseTask = instance.markTasksAsConcluded(pendingTask)

        verify(taskCall).enqueue(any())
        assertThat(responseTask.pending).isFalse
    }

    @Test
    fun `Should throw an exception when marking a tasks as finished given the body is not present in the response`() {
        val taskCall: Call<Task> = mock(Call::class.java) as Call<Task>
        val task = Task(1, 1.0f, true)

        Mockito.`when`(taskEndpoint.postPendingTasks(task)).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Task>).onResponse(taskCall, Response.success(null))
        }

        val exception = kotlin.runCatching {
            instance.markTasksAsConcluded(task)
        }.exceptionOrNull()

        assertThatIsAsExpected(exception, "Null body was returned by the POST tasks endpoint")
    }

    private fun assertThatIsAsExpected(exception: Throwable?, message: String) {
        assertThat(exception).isNotNull
        assertThat(exception!!.message).isEqualTo(message)
    }
}