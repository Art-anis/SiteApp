package com.nerazim.siteapp

import android.app.Application
import com.nerazim.siteapp.db.AppContainer
import com.nerazim.siteapp.db.AppDataContainer

//общий класс приложения
class SiteApplication: Application() {
    lateinit var container: AppContainer //контейнер, в котором лежит репозиторий

    //при запуске приложения создать контейнер
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}