package com.waterpump.manager.scheduler

import com.waterpump.manager.board.WaterPumpBoard
import com.waterpump.manager.ui.main.LogViewerWrapper

class WaterPumpScheduler(
    private val logViewerWrapper: LogViewerWrapper,
    private val waterPumpBoard: WaterPumpBoard) {

    fun startListening() {
        logViewerWrapper.log("Start listening to AWS")
        waterPumpBoard.startWaterPump()
    }


}
