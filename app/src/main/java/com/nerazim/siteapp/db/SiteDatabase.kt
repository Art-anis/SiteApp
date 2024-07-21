package com.nerazim.siteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//объект БД
@Database(entities = [SiteEntity::class], version = 1, exportSchema = false)
abstract class SiteDatabase: RoomDatabase() {
    //DAO
    abstract fun siteDao(): SiteDao

    companion object {
        @Volatile
        private var instance: SiteDatabase? = null //экземпляр БД - синглтон

        //получение
        fun getDatabase(context: Context): SiteDatabase {
            return instance ?: synchronized(this) { //если БД еще нет, создаем экземпляр и возвращаем его
                Room.databaseBuilder(context, SiteDatabase::class.java, "sites")
                    .build()
                    .also { instance = it }
            }
        }
    }
}