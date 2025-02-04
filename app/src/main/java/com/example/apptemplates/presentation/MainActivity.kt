package com.example.apptemplates.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.apptemplates.presentation.navigation.nav_graph.RootNavGraph
import com.example.apptemplates.utils.theme.AppTemplatesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            AppTemplatesTheme {
                val navController = rememberNavController()

                RootNavGraph(navController = navController)
            }
        }
    }
}








