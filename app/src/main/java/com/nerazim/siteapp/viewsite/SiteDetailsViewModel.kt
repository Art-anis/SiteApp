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

class SiteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    siteRepository: SiteRepository
): ViewModel() {
    private val itemId : Int = checkNotNull(savedStateHandle[DetailsDestination.itemId])

    val uiState: StateFlow<SiteDetailsUiState> =
        siteRepository.getSiteByIdStream(itemId)
            .filterNotNull()
            .map { SiteDetailsUiState(siteDetails = it.toSiteDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SiteDetailsUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SiteDetailsUiState(
    val siteDetails: SiteDetails = SiteDetails()
)