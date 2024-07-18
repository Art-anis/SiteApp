package com.nerazim.siteapp.addsite

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nerazim.siteapp.db.SiteEntity
import com.nerazim.siteapp.db.SiteRepository

class SiteEntryViewModel(private val siteRepository: SiteRepository): ViewModel() {
    var siteUiState by mutableStateOf(SiteUiState())
        private set

    fun updateUiState(siteDetails: SiteDetails) {
        siteUiState = SiteUiState(siteDetails = siteDetails, isEntryValid = validateInput(siteDetails))
    }

    suspend fun saveSite() {
        if (validateInput()) {
            siteRepository.insertSite(siteUiState.siteDetails.toSiteEntity())
        }
    }

    private fun validateInput(uiState: SiteDetails = siteUiState.siteDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

fun SiteDetails.toSiteEntity(): SiteEntity = SiteEntity(
    id = id,
    name = name,
    description = description,
    image = image.toString(),
    link = link
)

data class SiteUiState(
    val siteDetails: SiteDetails = SiteDetails(),
    val isEntryValid: Boolean = false
)

data class SiteDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: Uri = Uri.EMPTY,
    val link: String = ""
)