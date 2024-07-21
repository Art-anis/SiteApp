package com.nerazim.siteapp.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.SiteUiState
import com.nerazim.siteapp.addsite.toSiteDetails
import com.nerazim.siteapp.addsite.toSiteEntity
import com.nerazim.siteapp.db.SiteEntity
import com.nerazim.siteapp.db.SiteRepository
import com.nerazim.siteapp.nav.DetailsDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//viewModel редактирования - хранит редактируемое место
class SiteEditViewModel(
    savedStateHandle: SavedStateHandle, //сохраненное состояние
    private val siteRepository: SiteRepository //репозиторий
): ViewModel() {
    private val itemId : Int = checkNotNull(savedStateHandle[DetailsDestination.itemId]) //id текущего места

    var siteUiState by mutableStateOf(SiteUiState()) //объект состояния
        private set

    init { //конструктор
        viewModelScope.launch { //запуск корутины
            siteUiState = siteRepository.getSiteByIdStream(itemId) //получаем место по id
                .filterNotNull() //отфильровываем пустые
                .first() //выбираем первое
                .toSiteUiState(true) //приводим к нужному классу
        }
    }

    //валидация данных
    private fun validateInput(uiState: SiteDetails = siteUiState.siteDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() //название не должно быть пустым
        }
    }

    //функция обновления состония
    fun updateUiState(siteDetails: SiteDetails) {
        siteUiState = SiteUiState(siteDetails = siteDetails, isEntryValid = validateInput(siteDetails))
    }

    //функция сохранения
    suspend fun saveSite() {
        if (validateInput()) { //если валидация пройдена
            //обновить объект в БД
            siteRepository.updateSite(siteUiState.siteDetails.toSiteEntity())
        }
    }
}

fun SiteEntity.toSiteUiState(isEntryValid: Boolean = false): SiteUiState = SiteUiState(
    siteDetails = this.toSiteDetails(),
    isEntryValid = isEntryValid
)