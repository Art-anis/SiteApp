package com.nerazim.siteapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState

//главный экран
@Composable
fun HomeScreen(
    scaffoldState: MutableState<ScaffoldState>, //состояние шаблона
    goToAddScreen: () -> Unit, //функция перехода на экран добавления
    goToViewScreen: (Int) -> Unit, //функция перехода на экран просмотра места
    goToBrowseScreen: () -> Unit, //функция перехода на экран просмотра списка
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory) //viewModel, хранящая сгенерированное место
) {
    //обновление состояния шаблона
    scaffoldState.value = ScaffoldState(
        title = { //название
            Text(text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge //берем базовый стиль для заголовка
                    .merge(TextStyle( //делаем его жирным
                        fontWeight = FontWeight.Bold
                    ))
            )
        },
        topBarActions = { //кнопки верхнего меню
            IconButton(onClick = goToAddScreen) { //кнопка добавления
                Icon( //иконка
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
            IconButton(onClick = goToBrowseScreen) { //кнопка просмотра списка
                Icon( //иконка
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.browse_sites)
                )
            }
        },
        bottomBar = {} //нижнее меню пустое
    )

    //столбец со всем содержимым
    Column(
        modifier = Modifier
            .fillMaxWidth() //заполняем всю ширину экрана
            .verticalScroll(rememberScrollState()), //подключаем прокрутку
        horizontalAlignment = Alignment.CenterHorizontally, //выравнивание содержимого по центру
    ) {
        //картинка-логотип
        Image(
            painter = painterResource(id = R.drawable.city),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )

        //место под картинку сгенерированного места
        AsyncImage(
            model = viewModel.siteUiState.siteDetails.image, //данные берем из viewModel
            contentDescription = null,
            //если что-то не так или место еще не сгенерировано, отображаем другую картинку
            error = painterResource(id = R.drawable.placeholder),
            placeholder = painterResource(id = R.drawable.placeholder),
            fallback = painterResource(id = R.drawable.placeholder),
            modifier = Modifier
                .fillMaxWidth() //заполняем всю ширину столбца
                .padding(top = 16.dp) //отступ сверху
                .height(384.dp)
                .clickable(onClick = { //изображение кликабельно
                    val id = viewModel.siteUiState.siteDetails.id
                    if (id != 0) //если место есть, переходим на экран просмотра
                        goToViewScreen(id)
                })
        )

        //место для названия места
        Text(
            text = if (viewModel.siteUiState.siteDetails.name == "") //если место не сгенерировано,
                //вывести шаблонный текст
                stringResource(id = R.string.main_page_question)
                else viewModel.siteUiState.siteDetails.name,
            textAlign = TextAlign.Center, //выранивание по центру
            modifier = Modifier
                .border( //окружаем границей
                    width = 2.dp,
                    color = Color(0xFFD0BCFF),
                    shape = RoundedCornerShape(10.dp)
                )
                .width(200.dp) //фиксированная ширина
                .clickable { //текст также кликабельный
                    val id = viewModel.siteUiState.siteDetails.id
                    if (id != 0) //если место есть, переходим на экран просмотра
                        goToViewScreen(id)
                }
        )

        //кнопка для генерации
        Button(
            onClick = {
                viewModel.generate()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.generate),
                style = MaterialTheme.typography.titleMedium
            )
        }

        //текст "О нас"
        Text(
            text = stringResource(id = R.string.about_title),
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold)),
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.about_description),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}