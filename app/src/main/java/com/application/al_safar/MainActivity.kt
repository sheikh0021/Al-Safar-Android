package com.application.al_safar

import android.Manifest
import android.content.Intent
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
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.AlSafarTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

        val resolvedStartDestination = resolveNotificationDestination(intent)

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

    private fun resolveNotificationDestination(intent: Intent): String? {
        val targetScreen = intent.getStringExtra("target_screen") ?: return null
        val isLoggedIn = runBlocking { preferencesManager.isLoggedIn() }
        if (!isLoggedIn) return null

        val assignmentId = intent.getStringExtra("assignment_id")
            ?: parseAssignmentIdFromNotificationData(intent.getStringExtra("notification_data"))

        return when (targetScreen) {
            "chat_detail" -> {
                if (assignmentId != null) {
                    Screen.TravelerChat.createRoute(
                        assignmentId = assignmentId,
                        guideName = "Your Guide"
                    )
                } else {
                    Screen.TravelerMessages.route
                }
            }
            "my_guide" -> Screen.MyGuide.route
            "trip_dashboard" -> Screen.TripDashboard.route
            else -> targetScreen
        }
    }

    private fun parseAssignmentIdFromNotificationData(json: String?): String? {
        if (json.isNullOrBlank()) return null
        return try {
            val type = object : TypeToken<Map<String, String>>() {}.type
            val map: Map<String, String> = Gson().fromJson(json, type)
            map["assignment_id"]
        } catch (e: Exception) {
            null
        }
    }
}
