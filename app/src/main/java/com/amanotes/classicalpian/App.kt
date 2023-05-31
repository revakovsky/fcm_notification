package com.amanotes.classicalpian

import android.app.Application
import com.amanotes.classicalpian.di.appModule
import com.amanotes.classicalpian.di.gameModule
import com.onesignal.OneSignal
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(appModule, gameModule)
        }

        OneSignal.initWithContext(this)
        OneSignal.setAppId(getString(R.string.OneSignalAppId))
    }

}