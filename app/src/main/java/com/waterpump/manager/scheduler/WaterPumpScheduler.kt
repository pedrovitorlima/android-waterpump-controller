package com.waterpump.manager.scheduler

import com.waterpump.manager.api.ApiService
import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper

class WaterPumpScheduler(
    private val logViewerWrapper: LogViewerWrapper,
    private val waterPumpBoard: WaterPumpBoard,
    private val apiService: ApiService
) {

    fun startListening() {
        val pendingTasks = apiService.fetchPendingTasks()

        logViewerWrapper.log("Start listening to AWS")

        if (pendingTasks == null)
            return

        for (task in pendingTasks) {
            if (task != null) {
                waterPumpBoard.turnOnWaterPumpFor(task.seconds)
            }
        }
    }


}
