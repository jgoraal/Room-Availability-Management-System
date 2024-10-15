package com.example.apptemplates.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.apptemplates.navigation.route.AppScreen

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = AppScreen.Auth.route
    ) {
        authNavGraph(navHostController)
        mainNavGraph(navHostController)
    }
}
