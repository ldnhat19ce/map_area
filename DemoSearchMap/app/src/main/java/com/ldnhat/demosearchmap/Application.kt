package com.ldnhat.demosearchmap

import android.app.Application
import android.content.Context
import com.ldnhat.demosearchmap.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            androidLogger(Level.DEBUG)
            modules(listOf(viewModelModule))
            allowOverride(true)
        }
    }

}