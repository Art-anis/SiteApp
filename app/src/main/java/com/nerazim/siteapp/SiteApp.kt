package com.nerazim.siteapp

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.nerazim.siteapp.ui.theme.SiteAppTheme
import com.nerazim.siteapp.viewsite.ViewSiteScreen

data class ScaffoldState(
    val title: @Composable () -> Unit = {},
    val topBarActions: @Composable RowScope.() -> Unit = {},
    val bottomBar: @Composable () -> Unit = {}
)

sealed class Route(
    val path: String
) {
    data object Main: Route("Main")
    data object AddSite: Route("Add Site")
    data object ViewSite: Route("View Site")
    data object Browse: Route("Browse")
    data object EditSite: Route("Edit site")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteApp() {
    val navController = rememberNavController()

    val scaffoldState = remember {
        mutableStateOf(ScaffoldState())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = scaffoldState.value.title,
                actions = scaffoldState.value.topBarActions
            )
        },
        bottomBar = scaffoldState.value.bottomBar
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Route.Main.path
        ) {
           composable(Route.Main.path) {
               HomeScreen(
                   scaffoldState = scaffoldState,
                   goToAddScreen = {
                       navController.navigate(Route.AddSite.path)
                   },
                   goToViewScreen = {
                        navController.navigate("${DetailsDestination.route}/$it")
                   },
                   goToBrowseScreen = {
                       navController.navigate(Route.Browse.path)
                   }
               )
           }

            composable(Route.AddSite.path) {
                AddSiteScreen(
                    scaffoldState = scaffoldState,
                    goToBrowseScreen = {
                        navController.navigate(Route.Browse.path)
                    },
                    goBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(
                route = EditDestination.routeWithArgs,
                arguments = listOf(navArgument(EditDestination.itemId) {
                    type = NavType.IntType
                })
            ) {
                EditSiteScreen(
                    scaffoldState = scaffoldState,
                    goToBrowseScreen = {
                        navController.navigate(Route.Browse.path)
                    },
                    goBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(
                route = DetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(DetailsDestination.itemId) {
                    type = NavType.IntType
                })
            ) {
                ViewSiteScreen(
                    scaffoldState = scaffoldState,
                    goToAddScreen = {
                        navController.navigate(Route.EditSite.path)
                    },
                    goBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Route.Browse.path) {
                BrowseScreen(
                    scaffoldState = scaffoldState,
                    goToAddScreen = {
                        navController.navigate(Route.AddSite.path)
                    },
                    goToEditScreen = {
                        navController.navigate("${EditDestination.route}/$it")
                    },
                    goToViewScreen = {
                        navController.navigate("${DetailsDestination.route}/$it")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SiteAppPreview() {
    SiteAppTheme {
        SiteApp()
    }
}
