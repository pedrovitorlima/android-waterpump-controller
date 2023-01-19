package com.waterpump.manager.scheduler

import android.widget.TextView
import com.waterpump.manager.ui.main.LogViewerWrapper
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class WaterPumpSchedulerTest {
    @Mock
    lateinit var logViewerWrapper:LogViewerWrapper

    lateinit var instance:WaterPumpScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        instance = WaterPumpScheduler(logViewerWrapper)
    }

    @Test
    fun `Should log that application is listening to the endpoints`() {
        instance.startListening()
        verify(logViewerWrapper).log("Start listening to AWS")
    }
}