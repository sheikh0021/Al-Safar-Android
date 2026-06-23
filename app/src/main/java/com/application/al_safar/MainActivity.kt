package com.application.al_safar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.ui.navigation.NavGraph
import com.application.al_safar.ui.theme.AlSafarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Log.w("Permissions", "POST_NOTIFICATIONS permission denied")
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val targetScreen = intent.getStringExtra("target_screen")

        // Only navigate to a notification-driven screen if the user is already logged in.
        // If not logged in, fall back to null so NavGraph shows the Splash/Login flow.
        val resolvedStartDestination = if (targetScreen != null) {
            val isLoggedIn = runBlocking { preferencesManager.isLoggedIn() }
            if (isLoggedIn) targetScreen else null
        } else null

        setContent {
            AlSafarTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = resolvedStartDestination
                )
            }
        }
        askNotificationPermission()
    }
}
