package com.nerazim.siteapp.db

import android.content.Context

interface AppContainer {
    val sitesRepository: SiteRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val sitesRepository: SiteRepository by lazy {
        SiteRepositoryImpl(SiteDatabase.getDatabase(context).siteDao())
    }
}