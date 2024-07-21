package com.nerazim.siteapp.addsite

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.edit.SiteForm
import kotlinx.coroutines.launch

//экран добавления места
@Composable
fun AddSiteScreen(
    scaffoldState: MutableState<ScaffoldState>, //состояние шаблона
    goToBrowseScreen: () -> Unit, //функции перехода
    goBack: () -> Unit,
    viewModel: SiteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory) //viewModel для хранения свежесозданного места
) {
    val coroutineScope = rememberCoroutineScope() //область видимости корутины
    //обновление состояния шаблона
    scaffoldState.value = ScaffoldState(
        title = { //название
            Text(stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
                    .merge(
                        TextStyle(
                        fontWeight = FontWeight.Bold
                    ))
            )
        },
        topBarActions = { //кнопки верхнего меню
            IconButton(onClick = goToBrowseScreen) { //кнопка просмотра списка
                Icon( //иконка
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.browse_sites)
                )
            }
        },
        bottomBar = { //нижнее меню
            BottomAppBar {
                Row {
                    Button(onClick = { //кнопка сохранения
                        coroutineScope.launch { //запуск корутины
                            viewModel.saveSite()
                            goBack()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.save_btn))
                    }
                    Button(onClick = goBack) { //кнопка возврата
                        Text(text = stringResource(id = R.string.back_btn))
                    }
                }
            }
        }
    )

    //форма для заполнения данных
    SiteForm(
        headline = "Add a site of your choice",
        siteUiState = viewModel.siteUiState, //состояние из viewModel
        onValueChange = viewModel::updateUiState //метод обновления состояния
    )
}