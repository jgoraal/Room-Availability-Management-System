package com.example.apptemplates.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = AppScreen.Auth.route
    ) {
        authNavGraph(navHostController)
        mainNavGraph(navHostController) // Dodanie innej nawigacji, jeśli będzie potrzebna
    }
}
