package com.nerazim.siteapp.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.toSiteDetails
import com.nerazim.siteapp.db.SiteEntity
import com.nerazim.siteapp.db.SiteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//viewModel главного экрана - хранит случайно сгенерированное место
class HomeViewModel(private val siteRepository: SiteRepository): ViewModel() {
    var siteUiState by mutableStateOf(HomeUiState()) //состояние
        private set

    fun generate() { //функция генерации
        viewModelScope.launch {
            siteUiState = siteRepository //получение случайного места из БД
                .getRandomSite()
                .first() //выбираем "первое" (оно же единственное)
                .toHomeUiState() //приводим к нужному классу
        }
    }
}

//привод сущности из БД к классу состояния
fun SiteEntity.toHomeUiState(): HomeUiState = HomeUiState(
    siteDetails = this.toSiteDetails()
)

//класс состояния места
data class HomeUiState(
    val siteDetails: SiteDetails = SiteDetails()
)