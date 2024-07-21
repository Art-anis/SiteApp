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

class HomeViewModel(private val siteRepository: SiteRepository): ViewModel() {
    var siteUiState by mutableStateOf(HomeUiState())
        private set

    fun generate() {
        viewModelScope.launch {
            siteUiState = siteRepository
                .getRandomSite()
                .first()
                .toHomeUiState()
        }
    }
}

fun SiteEntity.toHomeUiState(): HomeUiState = HomeUiState(
    siteDetails = this.toSiteDetails()
)

data class HomeUiState(
    val siteDetails: SiteDetails = SiteDetails()
)