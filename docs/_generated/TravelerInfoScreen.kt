package com.application.al_safar.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.domain.model.TravelerInput
import com.application.al_safar.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelerInfoScreen(
    packageId: String,
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val draft by viewModel.draft.collectAsState()
    var travelers by remember(draft.numberOfTravelers) {
        mutableStateOf(List(draft.numberOfTravelers.coerceAtLeast(1)) { TravelerInput() })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Traveler Information") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Enter details for ${draft.numberOfTravelers} traveler(s)",
                style = MaterialTheme.typography.bodyLarge
            )

            travelers.forEachIndexed { index, traveler ->
                TravelerFormCard(
                    index = index + 1,
                    traveler = traveler,
                    onChange = { updated ->
                        travelers = travelers.toMutableList().also { it[index] = updated }
                    }
                )
            }

            Button(
                onClick = {
                    if (travelers.all { it.isValid() }) {
                        viewModel.setTravelers(travelers)
                        navController.navigate(Screen.ReviewBooking.createRoute(packageId))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = travelers.all { it.isValid() }
            ) {
                Text("Continue to Review")
            }
        }
    }
}

@Composable
private fun TravelerFormCard(
    index: Int,
    traveler: TravelerInput,
    onChange: (TravelerInput) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Traveler $index", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = traveler.fullName,
                onValueChange = { onChange(traveler.copy(fullName = it)) },
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = traveler.passportNumber,
                onValueChange = { onChange(traveler.copy(passportNumber = it)) },
                label = { Text("Passport number") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = traveler.nationality,
                onValueChange = { onChange(traveler.copy(nationality = it)) },
                label = { Text("Nationality") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = traveler.dateOfBirth,
                onValueChange = { onChange(traveler.copy(dateOfBirth = it)) },
                label = { Text("Date of birth (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = traveler.passportExpiry,
                onValueChange = { onChange(traveler.copy(passportExpiry = it)) },
                label = { Text("Passport expiry (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = traveler.gender,
                onValueChange = { onChange(traveler.copy(gender = it)) },
                label = { Text("Gender (male/female)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
