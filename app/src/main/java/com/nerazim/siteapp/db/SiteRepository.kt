package com.nerazim.siteapp.db

import kotlinx.coroutines.flow.Flow

//репозиторий
interface SiteRepository {
    //получение всех мест
    fun getAllSitesStream(): Flow<List<SiteEntity>>

    //получение места по id
    fun getSiteByIdStream(id: Int): Flow<SiteEntity?>

    //получение случайного места
    fun getRandomSite(): Flow<SiteEntity>

    //вставка
    suspend fun insertSite(site: SiteEntity)

    //обновление
    suspend fun updateSite(site: SiteEntity)

    //удаление
    suspend fun deleteSite(site: SiteEntity)
}

//реализация репозитория
class SiteRepositoryImpl(private val siteDao: SiteDao): SiteRepository {
    override fun getAllSitesStream(): Flow<List<SiteEntity>> = siteDao.getAll()

    override fun getSiteByIdStream(id: Int): Flow<SiteEntity?> = siteDao.getById(id)

    override fun getRandomSite(): Flow<SiteEntity> = siteDao.getRandom()

    override suspend fun insertSite(site: SiteEntity) = siteDao.insert(site)

    override suspend fun updateSite(site: SiteEntity) = siteDao.update(site)

    override suspend fun deleteSite(site: SiteEntity) = siteDao.delete(site)
}