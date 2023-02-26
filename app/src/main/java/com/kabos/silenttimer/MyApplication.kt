package com.kabos.silenttimer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureTimber()
    }

    private fun configureTimber() {
        // todo DEBUGの設定
        Timber.plant(Timber.DebugTree())
    }
}
