package com.waterpump.manager.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import retrofit2.Retrofit

class EndpointFactoryTest {

    @Test
    fun `Should create an instance of retrofit with the default url value for the API`() {
        val instance: Retrofit = EndpointFactory().retrofit

        val expectedUrl = "http://www.google.com/"
        val actualUrl:String = instance.baseUrl().toString()

        assertThat(actualUrl).isEqualTo(expectedUrl)
    }

    @Test
    fun `Should at least call the endpoint, even though it doesnt exist `() {
        val instance = EndpointFactory().createEndpoint(TaskEndpoints::class.java)
        val pendingTasks = instance.getPendingTasks()

        val execute = pendingTasks.execute()

        assertThat(execute.isSuccessful).isFalse()
        assertThat(execute.code()).isEqualTo(404)

    }
}