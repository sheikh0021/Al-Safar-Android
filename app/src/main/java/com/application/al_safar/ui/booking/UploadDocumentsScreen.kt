package com.application.al_safar.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocumentsScreen(
    bookingId: String,
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val bookingState by viewModel.bookingState.collectAsState()
    val uploadedDocs = remember { mutableStateMapOf<String, Boolean>() }

    val documentTypes = listOf(
        "passport" to "Passport",
        "visa" to "Visa",
        "photo" to "Photo ID",
        "other" to "Other Documents"
    )

    LaunchedEffect(bookingState) {
        if (bookingState is BookingUiState.DocumentUploaded) {
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Documents") },
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
            Text(
                text = "Please upload the required documents for your booking.",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Booking ID: $bookingId",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            documentTypes.forEach { (type, label) ->
                DocumentUploadCard(
                    label = label,
                    isUploaded = uploadedDocs[type] == true,
                    onUploadClick = {
                        // In production: launch file picker, then call viewModel.uploadDocument(...)
                        uploadedDocs[type] = true
                    }
                )
            }

            if (bookingState is BookingUiState.Loading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
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
                    navController.navigate(Screen.Payment.createRoute(bookingId)) {
                        popUpTo(Screen.UploadDocuments.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue to Payment")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DocumentUploadCard(
    label: String,
    isUploaded: Boolean,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUploaded)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.Upload,
                    contentDescription = null,
                    tint = if (isUploaded) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(label, style = MaterialTheme.typography.bodyLarge)
                    if (isUploaded) {
                        Text(
                            "Uploaded",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            if (!isUploaded) {
                OutlinedButton(onClick = onUploadClick) {
                    Text("Upload")
                }
            }
        }
    }
}
