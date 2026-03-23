package com.application.al_safar.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.application.al_safar.ui.navigation.Screen
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateGroupScreen(
    packageId: String,
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    var travelDateText by remember { mutableStateOf("") }
    var returnDateText by remember { mutableStateOf("") }
    var numberOfTravelers by remember { mutableStateOf(1) }
    var selectedGroupId by remember { mutableStateOf<String?>(null) }

    val bookingState by viewModel.bookingState.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val createdBookingId by viewModel.createdBookingId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(bookingState) {
        if (bookingState is BookingUiState.BookingCreated) {
            val bookingId = (bookingState as BookingUiState.BookingCreated).bookingId
            navController.navigate(Screen.UploadDocuments.createRoute(bookingId.toString())) {
                popUpTo(Screen.BookingDateGroup.route) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Dates & Group") },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Travel Dates",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = travelDateText,
                onValueChange = { travelDateText = it },
                label = { Text("Travel Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 2026-04-01") },
                singleLine = true
            )

            OutlinedTextField(
                value = returnDateText,
                onValueChange = { returnDateText = it },
                label = { Text("Return Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 2026-04-15") },
                singleLine = true
            )

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Number of Travelers", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (numberOfTravelers > 1) numberOfTravelers-- }
                    ) {
                        Icon(Icons.Default.Remove, "Decrease")
                    }
                    Text(
                        "$numberOfTravelers",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = { numberOfTravelers++ }) {
                        Icon(Icons.Default.Add, "Increase")
                    }
                }
            }

            if (groups.isNotEmpty()) {
                Divider()
                Text("Select Group (Optional)", style = MaterialTheme.typography.titleMedium)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedGroupId == null,
                            onClick = { selectedGroupId = null }
                        )
                        Text("No group (individual)")
                    }
                    groups.forEach { group ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedGroupId == group.id,
                                onClick = { selectedGroupId = group.id }
                            )
                            Text(group.name)
                        }
                    }
                }
            }

            if (bookingState is BookingUiState.Error) {
                Text(
                    text = (bookingState as BookingUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val travelDate = runCatching { LocalDate.parse(travelDateText) }.getOrNull()
                    val returnDate = runCatching { LocalDate.parse(returnDateText) }.getOrNull()
                    if (travelDate != null && returnDate != null) {
                        viewModel.createBooking(
                            packageId = packageId.toIntOrNull() ?: 0,
                            travelDate = travelDate,
                            returnDate = returnDate,
                            numberOfTravelers = numberOfTravelers,
                            groupId = selectedGroupId?.toIntOrNull()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = bookingState !is BookingUiState.Loading
                        && travelDateText.isNotBlank()
                        && returnDateText.isNotBlank()
            ) {
                if (bookingState is BookingUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Continue to Documents")
                }
            }
        }
    }
}
