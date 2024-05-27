package com.stromberg.cvstakehome.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stromberg.cvstakehome.model.FlickrItem
import com.stromberg.cvstakehome.model.FlickrItemType
import com.stromberg.cvstakehome.view.DetailsView
import com.stromberg.cvstakehome.view.SearchView

@Composable
fun CVSTakeHomeApp() {
    CVSTakeHomeNavHost()
}

@Composable
fun CVSTakeHomeNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Destination.Search.route) {
        composable(route = Destination.Search.route) {
            SearchView(navController)
        }
        composable(
            route = Destination.Details.route,
            arguments = listOf(
                navArgument("flickrItem") { type = FlickrItemType() }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getParcelable<FlickrItem>("flickrItem")?.let { flickrItem ->
                DetailsView(navController, flickrItem)
            }
        }
    }
}

enum class Destination(val route: String, val showBack: Boolean) {
    Search( "search", false),
    Details( "details", true),
}