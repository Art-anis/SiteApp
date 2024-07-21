package com.nerazim.siteapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//DAO с операциями
@Dao
interface SiteDao {
    //вставка
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(site: SiteEntity)

    //обновление
    @Update
    suspend fun update(site: SiteEntity)

    //удаление
    @Delete
    suspend fun delete(site: SiteEntity)

    //получение всех мест
    @Query("SELECT * FROM sites")
    fun getAll(): Flow<List<SiteEntity>>

    //получение места по id
    @Query("SELECT * FROM sites where id = :id")
    fun getById(id: Int): Flow<SiteEntity>

    //получение случайного места
    @Query("SELECT * FROM sites ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Flow<SiteEntity>
}