package com.waterpump.manager.api

import com.waterpump.manager.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class ApiServiceTest {

//    private val mockWebServer  = MockWebServer()
//
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(1, TimeUnit.SECONDS)
//        .readTimeout(1, TimeUnit.SECONDS)
//        .writeTimeout(1, TimeUnit.SECONDS)
//        .build()
//
//    private val api = Retrofit.Builder()
//        .baseUrl(BuildConfig.PUMP_TASK_API_URL)
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(TaskEndpoints::class.java)
//
//    @After
//    fun tearDown() {
//        mockWebServer.shutdown()
//    }

    @Test
    fun `Should create an instance of retrofit with the default url value for the API`() {
        val instance: Retrofit = ApiService().retrofit

        val expectedUrl = "http://www.google.com/"
        val actualUrl:String = instance.baseUrl().toString()

        assertThat(actualUrl).isEqualTo(expectedUrl)
    }

    @Test
    fun `Should at least call the endpoint, even though it doesnt exist `() {
        val instance = ApiService().buildService(TaskEndpoints::class.java)
        val pendingTasks = instance.getPendingTasks()

        val execute = pendingTasks.execute()

        assertThat(execute.isSuccessful).isFalse()
        assertThat(execute.code()).isEqualTo(404)

    }
}