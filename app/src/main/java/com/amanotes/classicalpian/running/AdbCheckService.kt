package com.amanotes.classicalpian.running

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings

class AdbCheckService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultIntent = Intent(ADB_INTENT_NAME).apply {
            putExtra(ADB_INTENT_TAG, getAdbStatus())
        }
        sendBroadcast(resultIntent)
        stopSelf()
        return START_STICKY
    }

    private fun getAdbStatus() =
        Settings.Secure.getInt(this.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1

}

const val ADB_INTENT_TAG = "is_adb_enabled"
const val ADB_INTENT_NAME = "adb_check_result"