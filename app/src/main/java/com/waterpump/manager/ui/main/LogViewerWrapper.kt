package com.waterpump.manager.ui.main

import android.widget.TextView

class LogViewerWrapper (logViewer: TextView) {
    private var logContent: String = ""
    private val logViewer: TextView = logViewer

    fun log(newLog: String) {
        logContent += "\n" + newLog
        logViewer.text = logContent
    }
}