package com.application.al_safar.ui.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.R
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg
import com.application.al_safar.ui.theme.Typography
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

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    if (showTravelDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showTravelDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = travelDatePickerState.selectedDateMillis
                        if (millis != null) {
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                            travelDateText = localDate.format(formatter)
                            returnDateText = localDate.plusDays(14).format(formatter)
                        }
                        showTravelDatePicker = false
                        localError = ""
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTravelDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = travelDatePickerState)
        }
    }

    if (showReturnDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showReturnDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = returnDatePickerState.selectedDateMillis
                        if (millis != null) {
                            returnDateText = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .format(formatter)
                        }
                        showReturnDatePicker = false
                        localError = ""
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = returnDatePickerState)
        }
    }

    Scaffold(containerColor = Emerald900) { padding ->
        BookingDateBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Spacer(modifier = Modifier.height(34.dp))

                BackButton(onClick = { navController.popBackStack() })

                HeaderSection()

                TravelDatesSection(
                    travelDateText = travelDateText,
                    returnDateText = returnDateText,
                    onTravelDateClick = { showTravelDatePicker = true },
                    onReturnDateClick = { showReturnDatePicker = true }
                )

                GoldDivider()

                TravelerSection(
                    numberOfTravelers = numberOfTravelers,
                    onDecrease = {
                        if (numberOfTravelers > 1) numberOfTravelers--
                    },
                    onIncrease = {
                        numberOfTravelers++
                    }
                )

                GroupInfoCard(
                    groupsAvailable = groups.isNotEmpty(),
                    selectedGroupId = selectedGroupId,
                    groupName = groups.firstOrNull { it.id == selectedGroupId }?.name,
                    onGroupToggle = {
                        selectedGroupId = if (selectedGroupId == null) {
                            groups.firstOrNull()?.id
                        } else {
                            null
                        }
                    }
                )

                ErrorMessages(
                    localError = localError,
                    bookingState = bookingState
                )

                Spacer(modifier = Modifier.height(46.dp))

                ContinueImageButton(
                    isLoading = bookingState is BookingUiState.Loading,
                    enabled = bookingState !is BookingUiState.Loading,
                    onClick = {
                        localError = validateAndSaveDraft(
                            travelDateText = travelDateText,
                            returnDateText = returnDateText,
                            packageId = packageId,
                            numberOfTravelers = numberOfTravelers,
                            selectedGroupId = selectedGroupId,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                )

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
private fun BookingDateBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.kaaba_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .align(Alignment.TopCenter)
                .alpha(0.34f),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Emerald900.copy(alpha = 0.88f),
                            Emerald900.copy(alpha = 0.96f),
                            Color(0xFF03281E)
                        )
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.faint_tomb),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(150.dp)
                .alpha(0.16f),
            contentScale = ContentScale.FillWidth
        )

        content()
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Text(
            text = "Select Dates & Group",
            style = Typography.headlineLarge,
            color = TextPrimaryDarkBg
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Choose your travel dates\nand group size",
            style = Typography.titleMedium,
            color = TextSecondaryDarkBg
        )

        Spacer(modifier = Modifier.height(26.dp))

        Box(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth(0.09f)
                .background(Gold500, RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun TravelDatesSection(
    travelDateText: String,
    returnDateText: String,
    onTravelDateClick: () -> Unit,
    onReturnDateClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        SectionTitle(
            icon = Icons.Default.CalendarMonth,
            title = "Travel Dates"
        )

        PremiumDateRow(
            title = if (travelDateText.isBlank()) "Select Travel Date" else travelDateText,
            onClick = onTravelDateClick
        )

        PremiumDateRow(
            title = if (returnDateText.isBlank()) "Select Return Date" else returnDateText,
            onClick = onReturnDateClick
        )
    }
}

@Composable
private fun PremiumDateRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Gold500.copy(alpha = 0.74f), RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Emerald700.copy(alpha = 0.75f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Gold500,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = title,
            style = Typography.titleMedium,
            color = Color(0xFFEFDDB8),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 22.dp)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Gold500,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun TravelerSection(
    numberOfTravelers: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SectionTitle(
            icon = Icons.Default.Groups,
            title = "Number of Travelers",
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier
                .height(66.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(28.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = onDecrease) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease travelers",
                    tint = Gold500
                )
            }

            Text(
                text = numberOfTravelers.toString(),
                style = Typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.size(42.dp)
            )

            IconButton(onClick = onIncrease) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase travelers",
                    tint = Gold500
                )
            }
        }
    }
}

@Composable
private fun GroupInfoCard(
    groupsAvailable: Boolean,
    selectedGroupId: String?,
    groupName: String?,
    onGroupToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .clickable(enabled = groupsAvailable, onClick = onGroupToggle)
            .padding(22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = Gold500,
                modifier = Modifier.size(34.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 18.dp)
        ) {
            Text(
                text = if (selectedGroupId == null) "Traveling with a group?" else "Group selected",
                style = Typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = groupName ?: if (groupsAvailable) {
                    "Tap here to attach your first available group."
                } else {
                    "You can add traveler details in the next step."
                },
                style = Typography.bodyMedium,
                color = TextSecondaryDarkBg
            )
        }
    }
}

@Composable
private fun ContinueImageButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(28.dp))
            .alpha(if (enabled) 1f else 0.65f)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.continue_button),
            contentDescription = "Continue to Documents",
            modifier = Modifier.fillMaxSize()
                .scale(scaleX = 1f, scaleY = 3.20f),
            contentScale = ContentScale.FillBounds
        )

        if (isLoading) {
            CircularProgressIndicator(color = Emerald900, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gold500,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = title,
            style = Typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GoldDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Gold500.copy(alpha = 0.42f))
        )
        Text(
            text = "*",
            color = Gold500,
            style = Typography.titleLarge,
            modifier = Modifier.padding(horizontal = 14.dp)
        )
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Gold500.copy(alpha = 0.42f))
        )
    }
}

@Composable
private fun ErrorMessages(
    localError: String,
    bookingState: BookingUiState
) {
    val message = when {
        localError.isNotBlank() -> localError
        bookingState is BookingUiState.Error -> bookingState.message
        else -> null
    }

    if (message != null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = message,
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

private fun validateAndSaveDraft(
    travelDateText: String,
    returnDateText: String,
    packageId: String,
    numberOfTravelers: Int,
    selectedGroupId: String?,
    viewModel: BookingViewModel,
    navController: NavController
): String {
    if (travelDateText.isBlank()) {
        return "Please select your travel date using the calendar."
    }

    if (returnDateText.isBlank()) {
        return "Please select your return date using the calendar."
    }

    val travelDate = runCatching { LocalDate.parse(travelDateText) }.getOrNull()
    val returnDate = runCatching { LocalDate.parse(returnDateText) }.getOrNull()

    if (travelDate == null || returnDate == null) {
        return "Invalid date. Please re-select your dates from the calendar."
    }

    if (!returnDate.isAfter(travelDate)) {
        return "Return date must be after the travel date."
    }

    viewModel.saveDraft(
        packageId = packageId.toIntOrNull() ?: 0,
        travelDate = travelDateText,
        returnDate = returnDateText,
        numberOfTravelers = numberOfTravelers,
        groupId = selectedGroupId?.toIntOrNull()
    )
    navController.navigate(Screen.TravelerInfo.createRoute(packageId))
    return ""
}