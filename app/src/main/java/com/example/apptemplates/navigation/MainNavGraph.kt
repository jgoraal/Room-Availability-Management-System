package com.example.apptemplates.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    navigation(
        route = AppScreen.Main.route,
        startDestination = AppScreen.Main.Home.route
    ) {
        composable(
            route = AppScreen.Main.Home.route
        ) {
            // Główny ekran po zalogowaniu
            HomeScreen(
                onLogout = {
                    navController.navigate(AppScreen.Auth.route) {
                        popUpTo(AppScreen.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
