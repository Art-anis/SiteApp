package com.nerazim.siteapp.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerazim.siteapp.db.SiteEntity
import com.nerazim.siteapp.db.SiteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrowseViewModel(private val siteRepository: SiteRepository): ViewModel() {
    val browseUiState: StateFlow<BrowseUiState> =
        siteRepository.getAllSitesStream()
            .map { BrowseUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BrowseUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteSite(id: Int) {
        viewModelScope.launch {
            val site: SiteEntity = siteRepository
                .getSiteByIdStream(id)
                .filterNotNull()
                .first()
            siteRepository.deleteSite(site)
        }
    }
}

data class BrowseUiState(val siteList: List<SiteEntity> = listOf())