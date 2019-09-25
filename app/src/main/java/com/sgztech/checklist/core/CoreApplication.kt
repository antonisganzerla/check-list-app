package com.sgztech.checklist.core

import android.app.Application
import com.sgztech.checklist.di.dbModule
import com.sgztech.checklist.di.repositoryModule
import com.sgztech.checklist.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule, uiModule))
        }
    }
}