package com.example.apptemplates.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.apptemplates.BottomBarPreview
import com.example.apptemplates.HomeScreen
import com.example.apptemplates.TopBarPreview
import com.example.apptemplates.navigation.RootNavGraph
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

                RootNavGraph(navHostController = navController)

            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun ScaffoldPreview() {

    Scaffold(
        topBar = {
            TopBarPreview()
        },

        content = { padding ->


            HomeScreen(padding)


        },

        bottomBar = {
            BottomBarPreview()
        }

    )
}
