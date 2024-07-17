package com.nerazim.siteapp.db

import kotlinx.coroutines.flow.Flow

interface SiteRepository {
    fun getAllSitesStream(): Flow<List<SiteEntity>>

    fun getSiteByIdStream(id: Int): Flow<SiteEntity?>

    suspend fun insertSite(site: SiteEntity)

    suspend fun updateSite(site: SiteEntity)

    suspend fun deleteSite(site: SiteEntity)
}

class SiteRepositoryImpl(private val siteDao: SiteDao): SiteRepository {
    override fun getAllSitesStream(): Flow<List<SiteEntity>> = siteDao.getAll()

    override fun getSiteByIdStream(id: Int): Flow<SiteEntity?> = siteDao.getById(id)

    override suspend fun insertSite(site: SiteEntity) = siteDao.insert(site)

    override suspend fun updateSite(site: SiteEntity) = siteDao.update(site)

    override suspend fun deleteSite(site: SiteEntity) = siteDao.delete(site)
}