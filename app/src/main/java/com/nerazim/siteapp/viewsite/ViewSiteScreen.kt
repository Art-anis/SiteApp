package com.nerazim.siteapp.viewsite

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState

//экран просмотра места
@Composable
fun ViewSiteScreen(
    scaffoldState: MutableState<ScaffoldState>, //состояние шаблона
    goToAddScreen: () -> Unit, //функции перехода
    goToEditScreen: (Int) -> Unit,
    goBack: () -> Unit,
    viewModel: SiteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory) //viewModel для места
) {
    val uiState = viewModel.uiState.collectAsState() //состояние объекта

    //обновляем состояние шаблона
    scaffoldState.value = ScaffoldState(
        title = { //заголовок
            Text(stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
                    .merge(TextStyle(
                        fontWeight = FontWeight.Bold
                    ))
            )
        },
        topBarActions = { //кнопки верхнего меню
            IconButton(onClick = goToAddScreen) { //кнопка добавления
                Icon( //иконка
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
            IconButton(onClick = { //кнопка редактирования
                goToEditScreen(uiState.value.siteDetails.id)
            }) {
                Icon( //иконка
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
            }
        },
        bottomBar = { //нижнее меню
            BottomAppBar {
                Button(onClick = goBack) { //кнопка возврата
                    Text(stringResource(id = R.string.back_btn))
                }
            }
        }
    )

    //отображение данных о месте
    SiteData(uiState.value)
}

//компонент для данных о месте
@Composable
fun SiteData(
    site: SiteDetailsUiState //данные
) {
    val context = LocalContext.current //контекст приложения
    Column( //столбец для отображения данных
        modifier = Modifier
            .fillMaxWidth() //заполняем всю ширину экрана
            .padding(horizontal = 10.dp) //отступ
            .verticalScroll(rememberScrollState()), //прокрутка
        horizontalAlignment = Alignment.CenterHorizontally //выравнивание по центру
    ) {
        //картинка-логотип
        Image(
            painter = painterResource(id = R.drawable.city),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )
        Spacer(Modifier.height(32.dp)) //отступ
        Text( //текст с названием
            text = site.siteDetails.name,
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        Spacer(modifier = Modifier.height(12.dp)) //отступ
        AsyncImage( //место под картинку
            model = site.siteDetails.image, //модель - URI из объекта
            contentDescription = null,
            //если нет картинки - отображаем иконку
            placeholder = rememberVectorPainter(image = Icons.Default.Place),
            error = rememberVectorPainter(image = Icons.Default.Place),
            fallback = rememberVectorPainter(image = Icons.Default.Place),
            modifier = Modifier
                .fillMaxWidth() //заполняем всю ширину
                .height(256.dp) //фиксированная высота
        )
        Spacer(modifier = Modifier.height(12.dp)) //отступ
        Text( //текст с описанием
            text = site.siteDetails.description,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text( //текст с ссылкой
            text = site.siteDetails.link,
            style = MaterialTheme.typography.bodyLarge
                .merge(
                    TextStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
            modifier = Modifier.clickable { //кликабельный
                try {
                    //открываем в браузере эту ссылку
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(site.siteDetails.link)))
                }
                catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "URL does not exist! Please fix the link.", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}