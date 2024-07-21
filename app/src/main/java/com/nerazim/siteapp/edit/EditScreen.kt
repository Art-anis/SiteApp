package com.nerazim.siteapp.edit

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.SiteUiState
import kotlinx.coroutines.launch

//экран редактирование места
@Composable
fun EditSiteScreen(
    scaffoldState: MutableState<ScaffoldState>, //состояние шаблона
    goToBrowseScreen: () -> Unit, //функции перехода
    goBack: () -> Unit,
    viewModel: SiteEditViewModel = viewModel(factory = AppViewModelProvider.Factory) //viewModel для редактируемого места
) {
    val coroutineScope = rememberCoroutineScope() //область видимости корутины
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
        topBarActions = { //кнопки верхнего меню
            IconButton(onClick = goToBrowseScreen) { //просмотр списка
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
                        coroutineScope.launch {
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
        headline = "Edit Site",
        siteUiState = viewModel.siteUiState,
        onValueChange = viewModel::updateUiState
    )
}


//форма для заполнения данных о месте
@SuppressLint("Recycle")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteForm(
    headline: String,
    siteUiState: SiteUiState, //состояние объекта места
    onValueChange: (SiteDetails) -> Unit //функция обновления данных
) {
    val context = LocalContext.current //контекст приложения для запуска выбора фотографии

    val photoPickerLauncher = rememberLauncherForActivityResult( //триггер запуска выбора фотографии
        contract = ActivityResultContracts.PickVisualMedia(), //выбираем из фотографий
        onResult = {
            if (it == null) return@rememberLauncherForActivityResult //проверка на null
            //записываем полученный файл в папку данных
            val input = context.contentResolver.openInputStream(it) ?: return@rememberLauncherForActivityResult
            val outputFile = context.filesDir.resolve(siteUiState.siteDetails.name)
            input.copyTo(outputFile.outputStream())

            //обновляем объект полученным URI
            onValueChange(siteUiState.siteDetails.copy(image = outputFile.toUri()))
        }
    )

    //столбец для отображения формы
    Column(
        modifier = Modifier
            .fillMaxWidth() //заполняем всю ширину экрана
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
        Text( //текст-заголовок
            text = headline,
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        Spacer(modifier = Modifier.height(6.dp)) //отступ

        //отдельный столбец для места под картинку с подписью
        Column(
            modifier = Modifier
                .fillMaxWidth() //заполняем всю ширину экрана
                .clickable { //кликабельный
                    if (siteUiState.siteDetails.name.isBlank()) { //если название пустое, выдать ошибку
                        Toast.makeText(context, "Write the site name first.", Toast.LENGTH_LONG).show()
                    }
                    else { //иначе запустить выбор фото
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally //выранивание по центру
        ) {
            //место под картинку
            AsyncImage(
                model = siteUiState.siteDetails.image, //источник - URI объекта
                contentDescription = null,
                //если что-то не так - отображаем иконку
                placeholder = rememberVectorPainter(image = Icons.Default.Place),
                error = rememberVectorPainter(image = Icons.Default.Place),
                fallback = rememberVectorPainter(image = Icons.Default.Place),
                modifier = Modifier
                    .fillMaxWidth() //заполняем всю ширину
                    .height(256.dp) //фиксированная высота
            )
            Spacer(Modifier.height(4.dp)) //отступ
            Text( //подпись к картинке
                text = stringResource(id = R.string.upload_image_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.5f)
            )
        }
        Spacer(Modifier.height(12.dp)) //отступ
        OutlinedTextField( //текстовое поле для названия
            value = siteUiState.siteDetails.name,
            onValueChange = { //при изменении обновить объект
                onValueChange(siteUiState.siteDetails.copy(name = it))
            },
            placeholder = { //подпись
                Text(
                    stringResource(id = R.string.name_field_placeholder),
                    modifier = Modifier.alpha(0.5f)
                )
            },
            //расцветка
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth(0.8f) //фиксированная ширина
        )
        Spacer(Modifier.height(12.dp)) //отступ
        OutlinedTextField( //текстовое поле для описания
            value = siteUiState.siteDetails.description,
            onValueChange = { //при изменении обновить объект
                onValueChange(siteUiState.siteDetails.copy(description = it))
            },
            placeholder = { //подпись
                Text(
                    stringResource(id = R.string.description_field_placeholder),
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors( //расцветка
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .height(150.dp) //фиксированные размеры и прокрутка
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(12.dp)) //отступ
        OutlinedTextField( //текстовое поле для ссылки
            value = siteUiState.siteDetails.link,
            onValueChange = { //при изменении обновить объект
                onValueChange(siteUiState.siteDetails.copy(link = it))
            },
            placeholder = { //подпись
                Text(
                    "Add link to website...",
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors( //расцветка
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(12.dp)) //отступ
    }
}