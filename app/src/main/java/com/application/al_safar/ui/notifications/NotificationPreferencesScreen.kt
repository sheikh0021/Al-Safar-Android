package com.application.al_safar.ui.notifications


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.data.local.entities.NotificationPreferenceEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPreferencesScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()
    val currentPrefs = preferences ?: NotificationPreferenceEntity()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Preferences") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PreferenceSection(
                title = "Booking Notifications",
                items = listOf(
                    "Confirm booking" to currentPrefs.notifyBookingConfirmation,
                    "Cancel booking" to currentPrefs.notifyBookingCancelled,
                    "Payment status" to currentPrefs.notifyPaymentStatus,
                ),
                onChanged = { label, value ->
                    viewModel.updatePreferences(
                        when (label) {
                            "Confirm booking" -> currentPrefs.copy(notifyBookingConfirmation = value)
                            "Cancel booking" -> currentPrefs.copy(notifyBookingCancelled = value)
                            "Payment status" -> currentPrefs.copy(notifyPaymentStatus = value)
                            else -> currentPrefs
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PreferenceSection(
                title = "Guide Notifications",
                items = listOf(
                    "Guide assigned" to currentPrefs.notifyGuideAssigned,
                    "Guide messages" to currentPrefs.notifyGuideMessages,
                ),
                onChanged = { label, value ->
                    viewModel.updatePreferences(
                        when (label) {
                            "Guide assigned" -> currentPrefs.copy(notifyGuideAssigned = value)
                            "Guide messages" -> currentPrefs.copy(notifyGuideMessages = value)
                            else -> currentPrefs
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PreferenceSection(
                title = "Trip Notifications",
                items = listOf(
                    "Trip reminders" to currentPrefs.notifyTripReminders,
                    "Group announcements" to currentPrefs.notifyGroupAnnouncements,
                ),
                onChanged = { label, value ->
                    viewModel.updatePreferences(
                        when (label) {
                            "Trip reminders" -> currentPrefs.copy(notifyTripReminders = value)
                            "Group announcements" -> currentPrefs.copy(notifyGroupAnnouncements = value)
                            else -> currentPrefs
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quiet Hours",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable quiet hours")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = currentPrefs.quietHoursEnabled,
                    onCheckedChange = { 
                        viewModel.updatePreferences(currentPrefs.copy(quietHoursEnabled = it))
                    }
                )
            }

            if (currentPrefs.quietHoursEnabled) {
                Text(
                    "${currentPrefs.quietHoursStart} - ${currentPrefs.quietHoursEnd}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PreferenceSection(
    title: String,
    items: List<Pair<String, Boolean>>,
    onChanged: (String, Boolean) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    items.forEach { (label, value) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = value,
                onCheckedChange = { onChanged(label, it) }
            )
        }
    }
}