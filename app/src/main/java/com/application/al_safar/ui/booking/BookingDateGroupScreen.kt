package com.application.al_safar.ui.booking

import android.widget.DatePicker
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    var showTravelDatePicker by remember { mutableStateOf(false) }
    var showReturnDatePicker by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }

    val travelDatePickerState = rememberDatePickerState()
    val returnDatePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val bookingState by viewModel.bookingState.collectAsState()
    val groups by viewModel.groups.collectAsState()

    //load available groups when the screen is first appears.
    //LaunchedEffect(Unit) runs exactly ONCE when the screen is first shown.
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

    if (showTravelDatePicker) {
        DatePickerDialog(
            onDismissRequest = {showTravelDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = travelDatePickerState.selectedDateMillis
                        if (millis != null) {
                            try {
                                val localDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                travelDateText = localDate.format(formatter)
                                returnDateText = localDate.plusDays(14).format(formatter)
                            } catch (e: Exception){
                                localError = "Error reading date. Please try again."
                            }
                        }
                        showTravelDatePicker = false
                        localError = ""
                    }
                ) { Text("OK")}
            },
            dismissButton = {
                TextButton(onClick = { showTravelDatePicker = false}) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = travelDatePickerState)
        }
    }

    if (showReturnDatePicker) {
        DatePickerDialog(
            onDismissRequest = {showReturnDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = returnDatePickerState.selectedDateMillis
                        if (millis != null) {
                            try {
                                returnDateText = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                    .format(formatter)
                            } catch (e: Exception){
                                localError = "Error reading date. Please try again."
                            }
                        }
                        showReturnDatePicker = false
                        localError = ""
                    }
                ) {Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {showReturnDatePicker}) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = returnDatePickerState)
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

            OutlinedButton(
                onClick = {showTravelDatePicker = true},
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = if (travelDateText.isBlank())
                        "Select Travel Date"
                     else
                        "Travel Date: $travelDateText"
                    ,
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedButton(
                onClick = {showReturnDatePicker = true},
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = if (returnDateText.isBlank())
                        "Select Return Date"
                     else
                        "Return Date: $returnDateText"
                    ,
                    modifier = Modifier.weight(1f)
                )
            }
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
                        modifier = Modifier.fillMaxWidth()) {
                        RadioButton(
                            selected = selectedGroupId == null,
                            onClick = { selectedGroupId = null })
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
            if (localError.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ $localError",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (bookingState is BookingUiState.Error) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❌ ${(bookingState as BookingUiState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    localError = ""
                   if (travelDateText.isBlank()){
                       localError = "Please Select your travel date using the calendar."
                       return@Button
                   }
                    if (returnDateText.isBlank()){
                        localError = "Please select your return date using the calendar."
                        return@Button
                    }
                  val travelDate = runCatching { LocalDate.parse(travelDateText) }.getOrNull()
                    val returnDate = runCatching { LocalDate.parse(returnDateText) }.getOrNull()

                    if (travelDate == null || returnDate == null) {
                        localError = "Invalid date. Please re-select your dates from the calendar."
                        return@Button
                    }

                    if (!returnDate.isAfter(travelDate)) {
                        localError = "Return date must be after the travel date."
                        return@Button
                    }

                    viewModel.createBooking(
                        packageId = packageId.toIntOrNull() ?: 0,
                        travelDate = travelDate,
                        returnDate = returnDate,
                        numberOfTravelers = numberOfTravelers,
                        groupId = selectedGroupId?.toIntOrNull()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = bookingState !is BookingUiState.Loading
            ) {
                if (bookingState is BookingUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary) } else {
                    Text("Continue to Documents")
                }
            }
        }
    }
}
