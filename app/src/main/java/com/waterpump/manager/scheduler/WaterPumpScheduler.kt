package com.waterpump.manager.scheduler

import com.waterpump.manager.ui.main.LogViewerWrapper

class WaterPumpScheduler(logViewerWrapper: LogViewerWrapper) {
    val logViewerWrapper = logViewerWrapper

    fun startListening() {
        logViewerWrapper.log("Start listening to AWS")
    }


}
