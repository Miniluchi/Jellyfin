package com.miniluchi.jellyfin.ui.navigation

object NavArgs {
    const val MOVIE_ID = "movieId"
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Detail : Screen("detail/{${NavArgs.MOVIE_ID}}") {
        fun routeFor(movieId: Int): String = "detail/$movieId"
    }
}
