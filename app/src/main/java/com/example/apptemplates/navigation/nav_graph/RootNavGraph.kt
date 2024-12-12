package com.example.apptemplates.navigation.nav_graph

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
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
                //delay(2.5.seconds)
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

    // Kolory gradientu - możesz dostosować je wg własnego uznania.
    val darkGradient = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
    val lightGradient = listOf(Color(0xFFFF9A9E), Color(0xFFFFD1C1), Color(0xFFFFE79A))

    // Stany koloru tła - przełączamy pomiędzy pierwszym i drugim zestawem kolorów.
    val isDarkTheme = isSystemInDarkTheme()
    val currentGradient by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Interpolacja kolorów gradientu
    val startColor by animateColorAsState(
        targetValue = if (isDarkTheme) {
            // Gradient plynnie zmienia się między dwoma kolorami
            Color.lerp(darkGradient[0], darkGradient[1], currentGradient)
        } else {
            Color.lerp(lightGradient[0], lightGradient[1], currentGradient)
        },
        animationSpec = tween(durationMillis = 1000)
    )

    val endColor by animateColorAsState(
        targetValue = if (isDarkTheme) {
            Color.lerp(darkGradient[1], darkGradient[2], currentGradient)
        } else {
            Color.lerp(lightGradient[1], lightGradient[2], currentGradient)
        },
        animationSpec = tween(durationMillis = 1000)
    )

    // Pulsacja tekstu
    val infinitePulse = rememberInfiniteTransition(label = "")
    val pulse by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Pulsacja ikony
    val iconPulse by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(startColor, endColor),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                tint = colors.onBackground,
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        scaleX = iconPulse
                        scaleY = iconPulse
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Proszę czekać...",
                color = colors.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                    }
                    .padding(bottom = 16.dp)
            )

            // Nowoczesny wskaźnik ładowania z grubszą kreską, może bardziej subtelny kolor
            CircularProgressIndicator(
                color = colors.onBackground.copy(alpha = 0.8f),
                strokeWidth = 6.dp,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

// Funkcja do interpolacji kolorów
private fun Color.Companion.lerp(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = lerp(start.red, end.red, fraction),
        green = lerp(start.green, end.green, fraction),
        blue = lerp(start.blue, end.blue, fraction),
        alpha = lerp(start.alpha, end.alpha, fraction)
    )
}
