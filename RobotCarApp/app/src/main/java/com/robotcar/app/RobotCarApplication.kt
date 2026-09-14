package com.robotcar.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RobotCarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
