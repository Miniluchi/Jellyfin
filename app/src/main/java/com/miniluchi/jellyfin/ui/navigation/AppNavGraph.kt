package com.miniluchi.jellyfin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miniluchi.jellyfin.ui.detail.DetailScreen
import com.miniluchi.jellyfin.ui.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.routeFor(movieId))
                },
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(NavArgs.MOVIE_ID) { type = NavType.IntType },
            ),
        ) {
            DetailScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
