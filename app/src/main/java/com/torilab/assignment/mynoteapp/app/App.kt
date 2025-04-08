package com.torilab.assignment.mynoteapp.app

import android.app.Application
import com.torilab.assignment.mynoteapp.app.di.Injector

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Injector.start(this)
    }
}
