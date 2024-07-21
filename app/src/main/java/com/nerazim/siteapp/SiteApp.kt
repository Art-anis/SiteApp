package com.nerazim.siteapp

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nerazim.siteapp.addsite.AddSiteScreen
import com.nerazim.siteapp.browse.BrowseScreen
import com.nerazim.siteapp.edit.EditSiteScreen
import com.nerazim.siteapp.home.HomeScreen
import com.nerazim.siteapp.nav.DetailsDestination
import com.nerazim.siteapp.nav.EditDestination
import com.nerazim.siteapp.viewsite.ViewSiteScreen

//класс состояния шаблона экрана
data class ScaffoldState(
    val title: @Composable () -> Unit = {}, //название, которое пишется в TopBar
    val topBarActions: @Composable RowScope.() -> Unit = {}, //кнопки-действия в TopBar
    val bottomBar: @Composable () -> Unit = {} //BottomBar (если есть)
)

//класс маршрутов для навигации
sealed class Route(
    val path: String //путь в графе навигации
) {
    data object Main: Route("Main") //главный экран
    data object AddSite: Route("Add Site") //экран добавления места
    data object Browse: Route("Browse") //экран просмотра списка
}

//коренной компонент
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteApp() {
    val navController = rememberNavController() //навигационный контроллер

    //инициализируем состояние шаблона
    val scaffoldState = remember {
        mutableStateOf(ScaffoldState())
    }

    //сам шаблон
    Scaffold(
        topBar = { //верхнее меню
            TopAppBar(
                title = scaffoldState.value.title, //название
                actions = scaffoldState.value.topBarActions, //действия из состояния
                //расцветка
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = scaffoldState.value.bottomBar //нижнее меню
    ) {
        //тело

        //навигация
        NavHost(
            modifier = Modifier.padding(it), //параметры стиля
            navController = navController, //контроллер
            startDestination = Route.Main.path //стартовая точка в графе
        ) {
            //главный экран
           composable(Route.Main.path) {
               //компонент главного экрана
               HomeScreen(
                   scaffoldState = scaffoldState, //состояние шаблона
                   goToAddScreen = { //функция перехода на экран добавления
                       navController.navigate(Route.AddSite.path)
                   },
                   goToViewScreen = { //функция перехода на экран просмотра места
                        navController.navigate("${DetailsDestination.route}/$it")
                   },
                   goToBrowseScreen = { //функция перехода на экран просмотра списка
                       navController.navigate(Route.Browse.path)
                   }
               )
           }

            //экран добавления места
            composable(Route.AddSite.path) {
                //компонент добавления места
                AddSiteScreen(
                    scaffoldState = scaffoldState, //состояние шаблона
                    goToBrowseScreen = { //функция перехода на экран просмотра списка
                        navController.navigate(Route.Browse.path)
                    },
                    goBack = { //функция возврата назад
                        navController.navigateUp()
                    }
                )
            }

            //экран редактирования места
            composable(
                route = EditDestination.routeWithArgs, //полный маршрут
                arguments = listOf(navArgument(EditDestination.itemId) { //аргумент - id места в БД
                    type = NavType.IntType
                })
            ) {
                //компонент экрана редактирования
                EditSiteScreen(
                    scaffoldState = scaffoldState, //состояние шаблона
                    goToBrowseScreen = { //функция перехода на экран просмотра списка
                        navController.navigate(Route.Browse.path)
                    },
                    goBack = { //функция возврата назад
                        navController.navigateUp()
                    }
                )
            }

            //экран просмотра места
            composable(
                route = DetailsDestination.routeWithArgs, //полный маршрут с аргументами
                arguments = listOf(navArgument(DetailsDestination.itemId) { //аргумент - id места в БД
                    type = NavType.IntType
                })
            ) {
                //компонент экрана просмотра места
                ViewSiteScreen(
                    scaffoldState = scaffoldState, //состояние шаблона
                    goToAddScreen = { //функция перехода на экран добавления
                        navController.navigate(Route.AddSite.path)
                    },
                    goToEditScreen = { //функция перехода на экран педактирования
                        navController.navigate("${EditDestination.route}/$it")
                    },
                    goBack = { //функция возврата назад
                        navController.navigateUp()
                    }
                )
            }

            //экран просмотра списка
            composable(Route.Browse.path) {
                //компонент экрана просмотра списка
                BrowseScreen(
                    scaffoldState = scaffoldState, //состояние шаблона
                    goToAddScreen = { //функция перехода на экран добавления
                        navController.navigate(Route.AddSite.path)
                    },
                    goToEditScreen = { //функция перехода на экран редактирования
                        navController.navigate("${EditDestination.route}/$it")
                    },
                    goToViewScreen = { //функция перехода на экран просмотра места
                        navController.navigate("${DetailsDestination.route}/$it")
                    },
                    goBack = { //функция возврата назад
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}