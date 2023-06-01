package com.amanotes.classicalpian.running

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amanotes.classicalpian.base.isInternetEnabled
import com.amanotes.classicalpian.ui.theme.FCM_LibraryTheme
import com.revakovskyi.fcm_notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RunningActivity : ComponentActivity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var notificationManager: NotificationManager? = null
    private var passedGadId = ""

    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                if (result.resultCode == Activity.RESULT_CANCELED) isInternetEnabled()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FCM_LibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RunningScreen(isInternetEnabled = isInternetEnabled()) {
                        settingsLauncher.launch(Intent(Settings.ACTION_WIFI_SETTINGS))
                        finish()
                    }
                }
            }
        }
        runLibrary()
    }

    private fun runLibrary() {
        val tag = "test"
        scope.launch(Dispatchers.IO) {
            val initializationStatus = initNotificationManager()
            Log.d("TAG_Max", "initializationStatus = $initializationStatus")

            if (initializationStatus != null && initializationStatus) {
                if (tag.isNotEmpty()) {
                    val status = notificationManager?.sendTag(tag)
                    Log.d("TAG_Max", "status = $status")
                }
            } else {
                Log.d("TAG_Max", "initializationStatus is negative")
            }
        }
    }

    private suspend fun initNotificationManager(): Boolean? {
        passedGadId = "232f4ed1-f187-4a6e-96e9-ec3ef30970d2"
        notificationManager = NotificationManager()
        return notificationManager?.init(
            passedGadId,
            packageName.toString()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        notificationManager = null
    }

}
