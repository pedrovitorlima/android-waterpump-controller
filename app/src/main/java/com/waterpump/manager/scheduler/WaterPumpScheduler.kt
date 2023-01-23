package com.waterpump.manager.scheduler

import com.waterpump.manager.api.ApiService
import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WaterPumpScheduler(
    private val logViewerWrapper: LogViewerWrapper,
    private val waterPumpBoard: WaterPumpBoard,
    private val apiService: ApiService
) {

    fun processPendingTasks() {
        val pendingTasks = apiService.fetchPendingTasks()

        logViewerWrapper.log("Start listening to AWS")

        if (pendingTasks.isEmpty()) {
            logViewerWrapper.log("No pending events to be processed")
            return
        }

        for (task in pendingTasks) {
            waterPumpBoard.turnOnWaterPumpFor(task.seconds)
            apiService.markTasksAsConcluded(task)
        }
    }


}
