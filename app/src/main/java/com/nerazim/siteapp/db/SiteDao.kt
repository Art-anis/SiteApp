package com.nerazim.siteapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(site: SiteEntity)

    @Update
    suspend fun update(site: SiteEntity)

    @Delete
    suspend fun delete(site: SiteEntity)

    @Query("SELECT * FROM sites")
    fun getAll(): Flow<List<SiteEntity>>

    @Query("SELECT * FROM sites where id = :id")
    fun getById(id: Int): Flow<SiteEntity>

    @Query("SELECT * FROM sites ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Flow<SiteEntity>
}