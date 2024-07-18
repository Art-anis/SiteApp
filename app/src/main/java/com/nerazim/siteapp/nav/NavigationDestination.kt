package com.nerazim.siteapp.nav

interface NavigationDestination {
    val route: String
}

object DetailsDestination: NavigationDestination {
    override val route: String = "site_details"
    const val itemId = "itemId"
    val routeWithArgs = "$route/{$itemId}"
}

object EditDestination: NavigationDestination {
    override val route: String = "edit_site"
    const val itemId = "itemId"
    val routeWithArgs = "${route}/{$itemId}"
}