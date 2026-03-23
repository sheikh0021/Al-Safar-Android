package com.application.al_safar.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    bookingId: String,
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    var transactionId by remember { mutableStateOf("") }
    val bookingState by viewModel.bookingState.collectAsState()
    val currentBooking by viewModel.currentBooking.collectAsState()

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId.toIntOrNull() ?: 0)
    }

    LaunchedEffect(bookingState) {
        if (bookingState is BookingUiState.PaymentConfirmed) {
            navController.navigate(Screen.Confirmation.createRoute(bookingId)) {
                popUpTo(Screen.Payment.route) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            currentBooking?.let { booking ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Booking Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Package: ${booking.umrahPackage.title}")
                        Text("Travel Date: ${booking.travelDate}")
                        Text("Return Date: ${booking.returnDate}")
                        Text("Travelers: ${booking.numberOfTravelers}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total: $${booking.totalAmount}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null)
                        Text(
                            text = "Payment Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Enter your transaction ID after completing payment through your bank or payment provider.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = transactionId,
                        onValueChange = { transactionId = it },
                        label = { Text("Transaction ID") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. TXN123456789") },
                        singleLine = true
                    )
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
                    viewModel.confirmPayment(
                        bookingId = bookingId.toIntOrNull() ?: 0,
                        transactionId = transactionId
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = transactionId.isNotBlank() && bookingState !is BookingUiState.Loading
            ) {
                if (bookingState is BookingUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Confirm Payment")
                }
            }
        }
    }
}
