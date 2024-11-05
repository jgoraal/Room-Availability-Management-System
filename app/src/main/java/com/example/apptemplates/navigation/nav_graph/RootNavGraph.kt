package com.example.apptemplates.navigation.nav_graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apptemplates.navigation.route.AppScreen
import com.example.apptemplates.presentation.login.login_menu.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = /*if (isUserAuthenticated) "main_graph" else*/ AppScreen.Loading.route
    ) {
        composable(AppScreen.Loading.route) {
            LoadingScreen(navController)
        }

        authNavGraph(navController)
        mainNavGraph(navController)
    }
}


@Composable
fun LoadingScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

    // Listen for authentication status
    LaunchedEffect(isUserAuthenticated) {
        when (isUserAuthenticated) {
            true -> {
                navController.navigate(AppScreen.Main.route) {
                    popUpTo(AppScreen.Loading.route) { inclusive = true }
                }
            }
            false -> {
                navController.navigate(AppScreen.Auth.route) {
                    popUpTo(AppScreen.Loading.route) { inclusive = true }
                }
            }
            else -> delay(200.milliseconds)
        }
    }

    // Loading screen layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Loading message
            Text(
                text = "Autoryzacja, proszę poczekać...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Customized circular progress indicator
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}



