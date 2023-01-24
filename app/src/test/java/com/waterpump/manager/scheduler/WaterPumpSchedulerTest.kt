package com.waterpump.manager.scheduler

import android.util.Log
import com.waterpump.manager.api.ApiService
import com.waterpump.manager.api.Task
import com.waterpump.manager.board.WaterPumpBoard
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class WaterPumpSchedulerTest {
    @Mock
    private lateinit var waterPumpBoard: WaterPumpBoard

    @Mock
    private lateinit var apiService:ApiService

    private lateinit var instance:WaterPumpScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        instance = WaterPumpScheduler(waterPumpBoard, apiService)

        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0

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
}