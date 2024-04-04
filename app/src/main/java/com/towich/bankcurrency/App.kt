package com.towich.bankcurrency

import android.app.Application
import android.content.Context
import com.towich.bankcurrency.data.repository.Repository

class App : Application() {
    val repository by lazy { Repository() }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}