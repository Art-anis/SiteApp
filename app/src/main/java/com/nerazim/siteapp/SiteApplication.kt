package com.nerazim.siteapp

import android.app.Application
import com.nerazim.siteapp.db.AppContainer
import com.nerazim.siteapp.db.AppDataContainer

class SiteApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}