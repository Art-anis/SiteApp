package com.nerazim.siteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nerazim.siteapp.ui.theme.SiteAppTheme

//главная (и единственная) активность приложения
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //вызываем onCreate родителя
        enableEdgeToEdge() //отображение контента от края до края экрана
        setContent {
            //содержимое оборачиваем в тему приложения
            SiteAppTheme {
                //коренной компонент
                SiteApp()
            }
        }
    }
}