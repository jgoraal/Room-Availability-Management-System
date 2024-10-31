package com.example.apptemplates.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.apptemplates.navigation.route.AppScreen

@Composable
fun RootNavGraph(navController: NavHostController, isUserAuthenticated: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (!isUserAuthenticated) "main_graph" else AppScreen.Auth.route
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
    }
}

