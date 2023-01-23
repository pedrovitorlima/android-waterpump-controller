package com.waterpump.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.waterpump.manager.api.ApiService
import com.waterpump.manager.api.EndpointFactory
import com.waterpump.manager.board.WaterPumpBoardImpl
import com.waterpump.manager.scheduler.WaterPumpScheduler
import com.waterpump.manager.ui.main.LogViewerWrapper
import com.waterpump.manager.ui.main.MainFragment
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        val logViewerWrapper: LogViewerWrapper = LogViewerWrapper(findViewById<TextView>(R.id.logViewer))
        logViewerWrapper.log("APPLICATION STARTED")

        //TODO start listening
        val waterPumpBoard = WaterPumpBoardImpl()
        val apiService = ApiService(EndpointFactory())

        val waterPumpScheduler = WaterPumpScheduler(logViewerWrapper, waterPumpBoard, apiService)

        Timer().schedule(5000) {
            waterPumpScheduler.processPendingTasks()
        }
    }
}