package com.example.apptemplates.navigation.nav_graph

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apptemplates.R
import com.example.apptemplates.navigation.route.AppScreen
import com.example.apptemplates.presentation.login.login_menu.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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

    LaunchedEffect(isUserAuthenticated) {
        when (isUserAuthenticated) {
            true -> {
                delay(2.5.seconds)
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

    val colors = MaterialTheme.colorScheme


    val gradientColors = if (isSystemInDarkTheme()) {
        // Kolory dla ciemnego motywu
        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
    } else {
        // Kolory dla jasnego motywu - pomarańczowo-żółto-różowe
        listOf(

            Color(0xFFFF9A9E),
            Color(0xFFFFD1C1),
            Color(0xFFFFE79A)


        )
    }

    // Animacja gradientu
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Animacja kropek w tekście
    val dotState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val dots = listOf("", ".", "..", "...")
        var index = 0
        while (true) {
            dotState.value = dots[index % dots.size]
            index++
            delay(500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, gradientOffset),
                    end = Offset(gradientOffset, 0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Opcjonalnie: Dodaj logo lub grafikę
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                tint = colors.onBackground,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tekst ładowania z animowanymi kropkami
            Text(
                text = "Proszę czekać${dotState.value}",
                color = colors.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Dostosowany wskaźnik ładowania
            CircularProgressIndicator(
                color = colors.onBackground,
                strokeWidth = 6.dp,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}