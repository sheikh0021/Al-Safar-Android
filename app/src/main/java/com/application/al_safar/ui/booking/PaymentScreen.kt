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
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    val bookingState by viewModel.bookingState.collectAsState()
    val currentBooking by viewModel.currentBooking.collectAsState()

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId.toIntOrNull() ?: 0)
    }

    LaunchedEffect(bookingState) {
        if (bookingState is BookingUiState.PaymentConfirmed) {
            navController.navigate(Screen.TripDashboard.route) {
                popUpTo(Screen.Home.route) { inclusive = false }
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
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total: ₹${booking.totalAmount}",
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Money, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "Cash Payment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cash (Pay to Agent manually)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "How it Works:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "1. Tap 'Confirm Cash Payment' Below",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "2. Your booking is confirmed instantly.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "3. Hand over the cash to your Al-Safar travel Agent before the departure date.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "4. The agent will mark your payment as received",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (bookingState is BookingUiState.Error) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❌${(bookingState as BookingUiState.Error).message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val cashTransactionId = "CASH_${System.currentTimeMillis()}"
                    viewModel.confirmPayment(
                        bookingId = bookingId.toIntOrNull() ?: 0,
                        transactionId = cashTransactionId,
                        paymentMethod = "cash"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled =  bookingState !is BookingUiState.Loading
            ) {
                if (bookingState is BookingUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Confirm your Cash Payment",
                        modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
