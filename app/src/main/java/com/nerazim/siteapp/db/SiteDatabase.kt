package com.nerazim.siteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SiteEntity::class], version = 1, exportSchema = false)
abstract class SiteDatabase: RoomDatabase() {
    abstract fun siteDao(): SiteDao

    companion object {
        @Volatile
        private var instance: SiteDatabase? = null

        fun getDatabase(context: Context): SiteDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, SiteDatabase::class.java, "sites")
                    .build()
                    .also { instance = it }
            }
        }
    }
}