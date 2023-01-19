package com.waterpump.manager.scheduler

import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class WaterPumpSchedulerTest {
    @Mock
    private lateinit var waterPumpBoard: WaterPumpBoard

    @Mock
    private lateinit var logViewerWrapper:LogViewerWrapper

    private lateinit var instance:WaterPumpScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        instance = WaterPumpScheduler(logViewerWrapper, waterPumpBoard)
    }

    @Test
    fun `Should log that application is listening to the endpoints`() {
        instance.startListening()
        verify(logViewerWrapper).log("Start listening to AWS")
    }

    @Test
    fun `Should trigger the water pump `() {

        instance.startListening()
        verify(waterPumpBoard).startWaterPump()

    }
}