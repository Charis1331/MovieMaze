package com.haris.houlis.moviemaze

import android.app.Application
import com.haris.houlis.moviemaze.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import org.koin.core.module.Module

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TestApp)
            modules(emptyList())
        }
    }

    internal fun loadModule(modules: List<Module>, block: () -> Unit) {
        loadKoinModules(modules)
        block()
        unloadKoinModules(modules)
    }
}