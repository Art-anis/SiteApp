package com.nerazim.siteapp.edit

import androidx.compose.runtime.collectAsState
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
import com.nerazim.siteapp.addsite.toSiteUiState
import com.nerazim.siteapp.db.SiteRepository
import com.nerazim.siteapp.nav.DetailsDestination
import com.nerazim.siteapp.nav.EditDestination
import com.nerazim.siteapp.viewsite.SiteDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SiteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val siteRepository: SiteRepository
): ViewModel() {
    private val itemId : Int = checkNotNull(savedStateHandle[DetailsDestination.itemId])

    var siteUiState by mutableStateOf(SiteUiState())
        private set

    init {
        viewModelScope.launch {
            siteUiState = siteRepository.getSiteByIdStream(itemId)
                .filterNotNull()
                .first()
                .toSiteUiState(true)
        }
    }

    private fun validateInput(uiState: SiteDetails = siteUiState.siteDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    fun updateUiState(siteDetails: SiteDetails) {
        siteUiState = SiteUiState(siteDetails = siteDetails, isEntryValid = validateInput(siteDetails))
    }

    suspend fun saveSite() {
        if (validateInput()) {
            siteRepository.updateSite(siteUiState.siteDetails.toSiteEntity())
        }
    }
}