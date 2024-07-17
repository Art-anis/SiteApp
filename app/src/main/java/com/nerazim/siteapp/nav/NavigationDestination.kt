package com.nerazim.siteapp.nav

interface NavigationDestination {
    val route: String
}

object DetailsDestination: NavigationDestination {
    override val route: String = "site_details"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}