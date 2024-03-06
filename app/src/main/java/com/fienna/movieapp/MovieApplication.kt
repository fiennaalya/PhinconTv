package com.fienna.movieapp

import android.app.Application
import com.fienna.movieapp.core.di.coreModule
import com.fienna.movieapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.NONE)
            androidContext(this@MovieApplication)
            modules(listOf(coreModule, appModule))
        }
    }
}
