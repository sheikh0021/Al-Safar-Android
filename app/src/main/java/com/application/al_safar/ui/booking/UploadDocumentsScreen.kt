package com.application.al_safar.ui.booking

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.util.BookingFileUtils
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocumentsScreen(
    bookingId: String,
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val bookingState by viewModel.bookingState.collectAsState()

    val uploadedDocs = remember { mutableStateMapOf<String, Boolean>() }
    var selectedDocumentType by remember { mutableStateOf<String?>(null) }
    var pendingCameraFile by remember { mutableStateOf<File?>(null) }
    var showSourcePicker by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }

    val requiredDocumentTypes = listOf(
        "passport" to "Passport",
        "photo" to "Photo ID"
    )

    val optionalDocumentTypes = listOf(
        "visa" to "Visa",
        "vaccine" to "Vaccination Certificate",
        "other" to "Other Documents"
    )

    val allRequiredUploaded = requiredDocumentTypes.all { (type, _) ->
        uploadedDocs[type] == true
    }

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val type = selectedDocumentType ?: return@rememberLauncherForActivityResult
        if (uri != null) {
            runCatching {
                BookingFileUtils.copyUriToCacheFile(context, uri, type)
            }.onSuccess { file ->
                viewModel.uploadDocument(
                    bookingId = bookingId.toIntOrNull() ?: 0,
                    documentType = type,
                    file = file
                )
            }.onFailure { error ->
                localError = error.message ?: "Could not read selected file."
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val type = selectedDocumentType ?: return@rememberLauncherForActivityResult
        val file = pendingCameraFile ?: return@rememberLauncherForActivityResult
        if (success) {
            viewModel.uploadDocument(
                bookingId = bookingId.toIntOrNull() ?: 0,
                documentType = type,
                file = file
            )
        } else {
            localError = "Camera capture was cancelled."
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val type = selectedDocumentType ?: return@rememberLauncherForActivityResult
        if (granted) {
            val file = BookingFileUtils.createCameraImageFile(context, type)
            pendingCameraFile = file
            cameraLauncher.launch(BookingFileUtils.getUriForFile(context, file))
        } else {
            localError = "Camera permission is required to capture a document."
        }
    }

    LaunchedEffect(bookingState) {
        if (bookingState is BookingUiState.DocumentUploaded) {
            selectedDocumentType?.let { uploadedDocs[it] = true }
            selectedDocumentType = null
            pendingCameraFile = null
            localError = ""
            viewModel.resetState()
        }
    }

    if (showSourcePicker) {
        AlertDialog(
            onDismissRequest = { showSourcePicker = false },
            title = { Text("Upload document") },
            text = { Text("Choose a file from your phone or capture a new photo.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSourcePicker = false
                        documentPickerLauncher.launch("*/*")
                    }
                ) {
                    Text("Choose File")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSourcePicker = false
                        val permission = Manifest.permission.CAMERA
                        val granted = ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED

                        if (granted) {
                            val type = selectedDocumentType ?: return@TextButton
                            val file = BookingFileUtils.createCameraImageFile(context, type)
                            pendingCameraFile = file
                            cameraLauncher.launch(BookingFileUtils.getUriForFile(context, file))
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    }
                ) {
                    Text("Camera")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Documents") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Upload Required Documents",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Booking ID: $bookingId",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Text(
                text = "Required",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            requiredDocumentTypes.forEach { (type, label) ->
                DocumentUploadCard(
                    label = label,
                    isRequired = true,
                    isUploaded = uploadedDocs[type] == true,
                    onUploadClick = {
                        selectedDocumentType = type
                        showSourcePicker = true
                    }
                )
            }

            Text(
                text = "Optional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            optionalDocumentTypes.forEach { (type, label) ->
                DocumentUploadCard(
                    label = label,
                    isRequired = false,
                    isUploaded = uploadedDocs[type] == true,
                    onUploadClick = {
                        selectedDocumentType = type
                        showSourcePicker = true
                    }
                )
            }

            if (bookingState is BookingUiState.Loading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (localError.isNotBlank()) {
                Text(
                    text = localError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (bookingState is BookingUiState.Error) {
                Text(
                    text = (bookingState as BookingUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.TravelerSummary.createRoute(bookingId)) {
                        popUpTo(Screen.UploadDocuments.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = allRequiredUploaded && bookingState !is BookingUiState.Loading
            ) {
                Text("Continue to Summary")
            }
        }
    }
}

@Composable
private fun DocumentUploadCard(
    label: String,
    isRequired: Boolean,
    isUploaded: Boolean,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUploaded) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isUploaded) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Description
                    },
                    contentDescription = null,
                    tint = if (isUploaded) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    },
                    modifier = Modifier.size(26.dp)
                )

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = when {
                            isUploaded -> "Uploaded"
                            isRequired -> "Required"
                            else -> "Optional"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUploaded) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                        }
                    )
                }
            }

            if (isUploaded) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Uploaded",
                    tint = MaterialTheme.colorScheme.secondary
                )
            } else {
                OutlinedButton(onClick = onUploadClick) {
                    Icon(
                        imageVector = Icons.Default.Upload,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Upload")
                }
            }
        }
    }
}