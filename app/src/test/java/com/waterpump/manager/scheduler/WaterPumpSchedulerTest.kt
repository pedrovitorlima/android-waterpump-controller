package com.waterpump.manager.scheduler

import com.waterpump.manager.api.ApiService
import com.waterpump.manager.api.Task
import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WaterPumpSchedulerTest {
    @Mock
    private lateinit var waterPumpBoard: WaterPumpBoard

    @Mock
    private lateinit var logViewerWrapper:LogViewerWrapper

    @Mock
    private lateinit var apiService:ApiService

    private lateinit var instance:WaterPumpScheduler


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        instance = WaterPumpScheduler(logViewerWrapper, waterPumpBoard, apiService)
    }

    @Test
    fun `Should log that application is listening to the endpoints`() {
        instance.processPendingTasks()
        verify(logViewerWrapper).log("Start listening to AWS")
    }

    @Test
    fun `Should turn on the water pump for the amount of time defined in the task`() {
        val listOfTasks:List<Task> = listOf(Task(1, 0.5f, false), Task(2, 0.5f, false))
        Mockito.`when`(apiService.fetchPendingTasks()).thenReturn(listOfTasks)

        instance.processPendingTasks()

        verify(waterPumpBoard, times(2)).turnOnWaterPumpFor(0.5f)
    }

    @Test
    fun `Should set task done after processing it`() {
        val task = Task(1, 0.5f, false)
        Mockito.`when`(apiService.fetchPendingTasks()).thenReturn(listOf(task))

        instance.processPendingTasks()

        verify(apiService).markTasksAsConcluded(task)
    }

    @Test
    fun `Should log if call the endpoint and there are no pending tasks`() {
        Mockito.`when`(apiService.fetchPendingTasks()).thenReturn(emptyList())

        instance.processPendingTasks()

        verify(logViewerWrapper).log("No pending events to be processed")
    }
}