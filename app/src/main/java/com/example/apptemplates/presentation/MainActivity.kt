package com.example.apptemplates.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.apptemplates.navigation.nav_graph.RootNavGraph
import com.example.apptemplates.presentation.login.login_menu.AuthViewModel
import com.example.apptemplates.ui.theme.AppTemplatesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        /*val splashScreen = installSplashScreen()

        // Use a variable to track when the delay is finished
        var keepSplashScreen = true

        // Set the condition to keep the splash screen on screen
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        // Launch a coroutine to delay the splash screen
        lifecycleScope.launch {
            delay(2000) // Keeps splash screen for 2 seconds
            keepSplashScreen = false
        }*/



        setContent {
            AppTemplatesTheme {
                val navController = rememberNavController()

                RootNavGraph(navController = navController)
            }
        }
    }
}








