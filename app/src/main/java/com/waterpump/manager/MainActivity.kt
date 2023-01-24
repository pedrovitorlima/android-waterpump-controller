package com.waterpump.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.waterpump.manager.api.ApiService
import com.waterpump.manager.api.EndpointFactory
import com.waterpump.manager.board.WaterPumpBoardImpl
import com.waterpump.manager.scheduler.WaterPumpScheduler
import com.waterpump.manager.ui.main.MainFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        Log.i("MyTag", "APPLICATION STARTED")

        val waterPumpBoard = WaterPumpBoardImpl()
        val apiService = ApiService(EndpointFactory())

        val waterPumpScheduler = WaterPumpScheduler(waterPumpBoard, apiService)

        val timer = Timer()

        timer.schedule(object: TimerTask() {
            override fun run() {
                waterPumpScheduler.processPendingTasks()
            }
        }, 0L,5*1000)
    }
}