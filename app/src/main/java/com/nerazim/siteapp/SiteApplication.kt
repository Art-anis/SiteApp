package com.nerazim.siteapp

import android.app.Application
import android.content.Context
import com.nerazim.siteapp.db.AppContainer
import com.nerazim.siteapp.db.AppDataContainer

class SiteApplication: Application() {
    lateinit var container: AppContainer

    init {
        instance = this
    }

    companion object {
        private var instance: SiteApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}