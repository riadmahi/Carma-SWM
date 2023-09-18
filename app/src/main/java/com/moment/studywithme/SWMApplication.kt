package com.moment.studywithme

import android.app.Application
import com.moment.studywithme.di.SWMAppInjector

class SWMApplication : Application() {

    private lateinit var swmAppInjector: SWMAppInjector

    override fun onCreate() {
        super.onCreate()
        swmAppInjector = SWMAppInjector(this)
    }

    fun getSWMAppInjector(): SWMAppInjector {
        return swmAppInjector
    }
}
