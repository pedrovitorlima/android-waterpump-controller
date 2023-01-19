package com.waterpump.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.waterpump.manager.scheduler.WaterPumpScheduler
import com.waterpump.manager.ui.main.LogViewerWrapper
import com.waterpump.manager.ui.main.MainFragment

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

        val waterPumpScheduler: WaterPumpScheduler = WaterPumpScheduler(logViewerWrapper)
        waterPumpScheduler.startListening()

    }
}