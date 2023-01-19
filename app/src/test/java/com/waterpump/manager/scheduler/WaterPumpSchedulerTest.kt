package com.waterpump.manager.scheduler

import com.waterpump.manager.api.ApiService
import com.waterpump.manager.api.Task
import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

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
        instance.startListening()
        verify(logViewerWrapper).log("Start listening to AWS")
    }

    @Test
    fun `Should turn on the water pump for the amount of time defined in the task`() {
        val listOfTasks:List<Task> = listOf(Task(1, 0.5f), Task(2, 0.5f))
        Mockito.`when`(apiService.fetchPendingTasks()).thenReturn(listOfTasks)

        instance.startListening()

        verify(waterPumpBoard, times(2)).turnOnWaterPumpFor(0.5f)
    }
}