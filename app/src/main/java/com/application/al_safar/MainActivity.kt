package com.application.al_safar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.application.al_safar.ui.navigation.NavGraph
import com.application.al_safar.ui.theme.AlSafarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val targetScreen = intent.getStringExtra("target_screen")
        val notificationData = intent.getStringExtra("notification_data")

        setContent {
            AlSafarTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = targetScreen
                )
            }
        }
    }
}
