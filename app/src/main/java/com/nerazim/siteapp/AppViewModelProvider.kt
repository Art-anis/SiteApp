package com.nerazim.siteapp

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nerazim.siteapp.addsite.SiteEntryViewModel
import com.nerazim.siteapp.browse.BrowseViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SiteEntryViewModel(siteApplication().container.sitesRepository)
        }

        initializer {
            BrowseViewModel(siteApplication().container.sitesRepository)
        }

//        initializer {
//            SiteDetailsViewModel(
//                this.createSavedStateHandle(),
//                siteApplication().container.sitesRepository
//            )
//        }
    }
}

fun CreationExtras.siteApplication(): SiteApplication =
    (this[APPLICATION_KEY] as SiteApplication)
