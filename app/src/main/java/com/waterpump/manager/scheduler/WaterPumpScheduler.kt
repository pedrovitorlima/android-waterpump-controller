package com.waterpump.manager.scheduler

import android.util.Log
import com.waterpump.manager.api.ApiService
import com.waterpump.manager.board.WaterPumpBoard

class WaterPumpScheduler(
    private val waterPumpBoard: WaterPumpBoard,
    private val apiService: ApiService
) {

    fun processPendingTasks() {
        try {
            val pendingTasks = apiService.fetchPendingTasks()

            Log.i("scheduler", "Listening to pending tasks")

            if (pendingTasks.isEmpty()) {
                Log.i("scheduler", "No pending events to be processed")
                return
            }

            for (task in pendingTasks) {
                waterPumpBoard.turnOnWaterPumpFor(task.seconds)
                apiService.markTasksAsConcluded(task)
            }
        } catch(exception:java.lang.Exception) {
            Log.i("scheduler", "An error has happened while fetching pending tasks: " + exception.message!!)
        }
    }


}
