package com.slimdroid.ipcaidl.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class IpcService : Service() {

    private var resolver: TaskResolver? = null

    override fun onCreate() {
        super.onCreate()
        resolver = TaskResolver()
    }

    override fun onBind(intent: Intent?): IBinder? = resolver

    override fun onDestroy() {
        resolver = null
    }

}