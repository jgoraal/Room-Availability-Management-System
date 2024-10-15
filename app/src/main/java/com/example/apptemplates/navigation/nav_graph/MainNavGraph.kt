package com.example.apptemplates.navigation.nav_graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.apptemplates.navigation.route.AppScreen
import com.example.apptemplates.presentation.main.home.HomeScreen
import com.example.apptemplates.presentation.main.home.HomeViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(
        route = AppScreen.Main.route,
        startDestination = AppScreen.Main.Home.route
    ) {
        homeComposable(navController)
        // Add more composables as the app grows
    }
}

private fun NavGraphBuilder.homeComposable(navController: NavController) {
    composable(route = AppScreen.Main.Home.route) {

        val homeViewModel: HomeViewModel = viewModel()


        HomeScreen(
            viewModel = homeViewModel,
            navigateToReservation = { navController.navigate(AppScreen.Main.Reservation.route) },
            navigateToProfile = { navController.navigate(AppScreen.Main.Profile.route) },
            navigateToSettings = { navController.navigate(AppScreen.Main.Settings.route) },
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


private fun NavGraphBuilder.reservationComposable(navController: NavController) {
    composable(route = AppScreen.Main.Reservation.route) {
    }
}


private fun NavGraphBuilder.profileComposable(navController: NavController) {
    composable(route = AppScreen.Main.Profile.route) {
    }
}


private fun NavGraphBuilder.settingsComposable(navController: NavController) {
    composable(route = AppScreen.Main.Settings.route) {
    }
}




