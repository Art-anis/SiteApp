package com.nerazim.siteapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

//сущность места в БД
@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, //название
    val description: String, //описание
    val image: String, //URI картинки
    val link: String //ссылка
)