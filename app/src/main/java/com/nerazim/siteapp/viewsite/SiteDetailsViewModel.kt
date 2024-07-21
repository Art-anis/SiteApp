package com.nerazim.siteapp.viewsite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.toSiteDetails
import com.nerazim.siteapp.db.SiteRepository
import com.nerazim.siteapp.nav.DetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

//viewModel для экрана просмотра места - хранит место, которые мы хотим посмотреть
class SiteDetailsViewModel(
    savedStateHandle: SavedStateHandle, //сохраненное состояние
    siteRepository: SiteRepository //репозиторий с данными
): ViewModel() {
    private val itemId : Int = checkNotNull(savedStateHandle[DetailsDestination.itemId]) //id текущего места

    val uiState: StateFlow<SiteDetailsUiState> = //получаем данные
        siteRepository.getSiteByIdStream(itemId) //из репозитория
            .filterNotNull() //отфильтровываем пустые значения
            .map { SiteDetailsUiState(siteDetails = it.toSiteDetails()) } //преобразовываем к нужному нам классу
            .stateIn( //преобразовываем обычный Flow в StateFlow
                scope = viewModelScope, //область наблюдаемости
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), //стратегия осведомления об изменениях
                initialValue = SiteDetailsUiState() //пустое начальное значение
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SiteDetailsUiState(
    val siteDetails: SiteDetails = SiteDetails()
)