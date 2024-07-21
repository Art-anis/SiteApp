package com.nerazim.siteapp.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.toSiteDetails
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

//экран просмотра списка
@Composable
fun BrowseScreen(
    scaffoldState: MutableState<ScaffoldState>, //состояние шаблона
    goToAddScreen: () -> Unit, //функция перехода на экран добавления
    goToEditScreen: (Int) -> Unit, //функция перехода на экран редактирования
    goToViewScreen: (Int) -> Unit, //функция перехода на экран просмотра места
    goBack: () -> Unit, //функция возврата назад
    viewModel: BrowseViewModel = viewModel(factory = AppViewModelProvider.Factory) //viewModel для хранения списка
) {
    //обновление состояния шаблона
    scaffoldState.value = ScaffoldState(
        title = { //название
            Text(stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
                    .merge(TextStyle(
                        fontWeight = FontWeight.Bold
                    ))
            )
        },
        topBarActions = { //действия верхнего меню
            IconButton(onClick = goToAddScreen) { //кнопка добавления
                Icon( //иконка
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
        },
        bottomBar = { //нижнее меню
            BottomAppBar {
                Button(onClick = goBack) { //кнопка возврата назад
                    Text(stringResource(id = R.string.back_btn))
                }
            }
        }
    )

    val browseUiState by viewModel.browseUiState.collectAsState() //собираем состояние из viewModel

    //столбец со списком мест
    LazyColumn {
        items(browseUiState.siteList) { site ->
            //компонент для отображения отдельного места
            SiteItem(
                site = site.toSiteDetails(), //данные
                goToEditScreen = goToEditScreen, //фунцкии перехода
                goToViewScreen = goToViewScreen,
                deleteSite = viewModel::deleteSite //функция удаления
            )
        }
    }
}

//компонент для отображения отдельного места
@Composable
fun SiteItem(
    deleteSite: KSuspendFunction1<Int, Unit>, //функция удаления
    site: SiteDetails, //данные
    goToEditScreen: (Int) -> Unit, //функции перехода
    goToViewScreen: (Int) -> Unit
) {
    //переменная-флаг для отображения диалога
    val showDialog = remember {
        mutableStateOf(false)
    }

    //если надо, отображаем диалог-подтверждения удаления
    if (showDialog.value) {
        //сам диалог
        DeleteConfirmation(
            deleteSite = deleteSite, //функция удаления
            site = site, //место
            onDismiss = { //функция при закрытии диалога
                showDialog.value = false
            }
        )
    }

    //строка со всеми данным
    Row(
        modifier = Modifier
            .fillMaxWidth() //заполняем всю ширину экрана
            .height(150.dp) //фиксированная высота
            .clickable { //кликабельный
                goToViewScreen(site.id) //переход к экрану просмотра этого места
            }
    ) {
        //столбец под картинку
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f) //занимает половину строки
                .padding( //отступы
                    start = 10.dp,
                    end = 10.dp
                )
        ) {
            //сама картинка
            AsyncImage(
                model = site.image,
                contentDescription = null,
                //если картинки нет, отобразить шаблонную иконку
                placeholder = rememberVectorPainter(image = Icons.Default.Place),
                error = rememberVectorPainter(image = Icons.Default.Place),
                fallback = rememberVectorPainter(image = Icons.Default.Place),
                modifier = Modifier.fillMaxSize() //картинка заполняет весь столбец
            )
        }
        //столбец с данными
        Column(
            modifier = Modifier.fillMaxSize() //заполняет все свободное место
        ) {
            Spacer(modifier = Modifier.height(10.dp)) //отступ
            Text( //текст с названием места
                text = site.name,
                style = MaterialTheme.typography.titleLarge //стиль - жирный заголовок
                    .merge(
                        TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ),
                textAlign = TextAlign.Center, //выравнивание по центру
                modifier = Modifier.height(30.dp), //фиксированная высота
                maxLines = 2, //максимум две строчки
                overflow = TextOverflow.Ellipsis //показывать многоточие, если больше текста
            )
            Spacer(modifier = Modifier.height(10.dp)) //отступ
            Text( //текст с описанием
                text = site.description,
                maxLines = 2, //максимум 2 строчки
                overflow = TextOverflow.Ellipsis, //показывать многоточие, если больше текста
                modifier = Modifier.height(50.dp) //фиксированная высота
            )
            Spacer(modifier = Modifier.height(10.dp)) //отступ
            Row( //ряд кнопок
                modifier = Modifier
                    .fillMaxWidth() //заполняет всю ширину столбца
                    .height(30.dp), //фиксированная высота
                horizontalArrangement = Arrangement.Center //выравнивание по центру
            ) {
                IconButton(onClick = { //кнопка редактирования
                    goToEditScreen(site.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
                IconButton(onClick = { //кнопка удаления
                    showDialog.value = true //показать диалог
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

//диалог-подтверждение удаления
@Composable
fun DeleteConfirmation(
    deleteSite: KSuspendFunction1<Int, Unit>, //функция удаления
    site: SiteDetails, //данные
    onDismiss: () -> Unit //функция при закрытии диалога
) {
    val coroutineScope = rememberCoroutineScope() //область видимости корутины для удаления
    //сам диалог
    AlertDialog(
        title = { //заголовок
            Text("Delete site?")
        },
        text = { //запрос
            Text("Are you sure you want to delete this site?")
        },
        onDismissRequest = onDismiss, //что делать, если диалог закрыть, не нажав на кнопку "Да"
        dismissButton = { //кнопка отказа
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        },
        confirmButton = { //кнопка согласия
            TextButton(onClick = {
                //запустить удаление места
                coroutineScope.launch {
                    deleteSite(site.id)
                    onDismiss()
                }
            }) {
                Text(text = "Yes")
            }
        },
    )
}