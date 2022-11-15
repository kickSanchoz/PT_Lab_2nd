package ru.pt_lab_2nd.android

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        res = resources
    }

    companion object {
        lateinit var res: Resources
    }
}