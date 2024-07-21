package com.nerazim.siteapp.nav

//формат пути в навигации для просмотра и редактирования места
interface NavigationDestination {
    val route: String
}

//путь для просмотра
object DetailsDestination: NavigationDestination {
    override val route: String = "site_details"
    const val itemId = "itemId"
    val routeWithArgs = "$route/{$itemId}"
}

//путь для редактирования
object EditDestination: NavigationDestination {
    override val route: String = "edit_site"
    const val itemId = "itemId"
    val routeWithArgs = "${route}/{$itemId}"
}