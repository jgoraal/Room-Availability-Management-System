package com.example.apptemplates.presentation.navigation.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

import com.example.apptemplates.presentation.navigation.route.AppScreen
import com.example.apptemplates.presentation.screens.home.base.AppScaffold

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        route = "main_graph",
        startDestination = AppScreen.Main.route
    ) {
        composable(AppScreen.Main.route) {
            val bottomNavController = rememberNavController()
            AppScaffold(navController = bottomNavController) {
                navController.navigate(AppScreen.Auth.route) {
                    popUpTo("main_graph") { inclusive = true } // Reset stosu do ekranu logowania
                }
            }
        }
    }
}






