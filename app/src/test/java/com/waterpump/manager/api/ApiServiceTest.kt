package com.waterpump.manager.api

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response


class ApiServiceTest {

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
        val taskCall: Call<*> = mock(Call::class.java)
        val response:Response<*> = mock(Response::class.java)
        val tasks:Tasks = Tasks(listOf(Task(1, 0.0f)))

        Mockito.`when`(endpointFactory.createEndpoint(TaskEndpoints::class.java)).thenReturn(taskEndpoint)
        Mockito.`when`(taskEndpoint.getPendingTasks()).thenReturn(taskCall as Call<Tasks>)
        Mockito.`when`(taskCall.execute()).thenReturn(response as Response<Tasks>)
        Mockito.`when`(response.body()).thenReturn(tasks)

        val fetchPendingTasks = instance.fetchPendingTasks()

        assertThat(fetchPendingTasks).hasSize(1)
    }

    @Test
    fun `Should set pending tasks as completed when the related endpoint is called`() {

    }
}