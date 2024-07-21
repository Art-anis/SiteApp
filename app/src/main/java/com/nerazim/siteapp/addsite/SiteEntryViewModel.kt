package com.nerazim.siteapp.addsite

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nerazim.siteapp.db.SiteEntity
import com.nerazim.siteapp.db.SiteRepository

//viewModel для создания - хранит создаваемый объект
class SiteEntryViewModel(private val siteRepository: SiteRepository): ViewModel() {
    var siteUiState by mutableStateOf(SiteUiState()) //объект состояния
        private set

    //функция обновления
    fun updateUiState(siteDetails: SiteDetails) {
        siteUiState = SiteUiState(siteDetails = siteDetails, isEntryValid = validateInput(siteDetails))
    }

    //функция сохранения
    suspend fun saveSite() {
        if (validateInput()) { //если валидация пройдена, создать объект в БД
            siteRepository.insertSite(siteUiState.siteDetails.toSiteEntity())
        }
    }

    //валидация данных
    private fun validateInput(uiState: SiteDetails = siteUiState.siteDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() //название не должно быть пустым
        }
    }
}

//привод класса данных к классу сущности
fun SiteDetails.toSiteEntity(): SiteEntity = SiteEntity(
    id = id,
    name = name,
    description = description,
    image = image.toString(),
    link = link
)

//привод класса сущности к классу данных
fun SiteEntity.toSiteDetails(): SiteDetails = SiteDetails(
    id = id,
    name = name,
    description = description,
    image = Uri.parse(image),
    link = link
)

//класс состояния
data class SiteUiState(
    val siteDetails: SiteDetails = SiteDetails(),
    val isEntryValid: Boolean = false
)

//класс данных
data class SiteDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: Uri = Uri.EMPTY,
    val link: String = ""
)