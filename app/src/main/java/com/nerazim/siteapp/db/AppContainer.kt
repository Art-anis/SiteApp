package com.nerazim.siteapp.db

import android.content.Context

//контейнер, который хранит в себе репозиторий
interface AppContainer {
    val sitesRepository: SiteRepository
}

//реализация интерфейса контейнера
class AppDataContainer(private val context: Context): AppContainer {
    override val sitesRepository: SiteRepository by lazy { //ленивая инициализация
        SiteRepositoryImpl(SiteDatabase.getDatabase(context).siteDao())
    }
}