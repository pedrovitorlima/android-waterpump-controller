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
        val taskEndpoint = mock(TaskEndpoints::class.java)
        val taskCall: Call<Tasks> = mock(Call::class.java) as Call<Tasks>
        val tasks:Tasks = Tasks(listOf(Task(1, 0.0f)))

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java)).thenReturn(taskEndpoint)
        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall)

        whenever(taskCall.enqueue(any())).thenAnswer {
            (it.arguments[0] as Callback<Tasks>).onResponse(taskCall, retrofit2.Response.success(tasks))
        }

        val fetchPendingTasks = instance.fetchPendingTasks()

        assertThat(fetchPendingTasks).hasSize(1)
    }

    @Test
    fun `Should set pending tasks as completed when the related endpoint is called`() {

    }
}