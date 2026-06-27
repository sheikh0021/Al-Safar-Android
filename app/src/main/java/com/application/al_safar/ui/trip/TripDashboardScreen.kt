package com.application.al_safar.ui.trip

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.R
import com.application.al_safar.domain.model.Booking
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.AlSafarGreenLight
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.RafiqBgBottom
import com.application.al_safar.ui.theme.RafiqBgTop
import com.application.al_safar.ui.theme.RafiqHeaderScrim
import com.application.al_safar.ui.theme.RafiqWelcomeCardBg
import com.application.al_safar.ui.theme.RafiqWelcomeCardBorder
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private enum class TripTab { UPCOMING, PAST }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TripDashboardScreen(
    navController: NavController,
    viewModel: TripDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(TripTab.UPCOMING) }

    Box(modifier = Modifier.fillMaxSize()) {
        TripDashboardBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            TripDashboardHeader()

            TripTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            when (val state = uiState) {
                is TripDashboardUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Gold500)
                    }
                }

                is TripDashboardUiState.Success -> {
                    val filtered = filterBookings(state.bookings, selectedTab)

                    if (filtered.isEmpty()) {
                        TripEmptyState(selectedTab = selectedTab)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(filtered, key = { it.id }) { booking ->
                                TripBookingCard(
                                    booking = booking,
                                    onClick = {
                                        if (booking.paymentStatus == "pending") {
                                            navController.navigate(
                                                Screen.Payment.createRoute(booking.id.toString())
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                is TripDashboardUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            fontFamily = Poppins,
                            color = Color(0xFFFF8A80),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripDashboardBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RafiqBgTop, RafiqBgBottom)
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.kaaba_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
            alpha = 0.85f
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            RafiqHeaderScrim.copy(alpha = 0.55f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
private fun TripDashboardHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp, bottom = 20.dp)
    ) {
        Text(
            text = "My Trips",
            fontFamily = Playfair,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = TextPrimaryDarkBg
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "All your Umrah journeys in one place",
            fontFamily = Poppins,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = TextSecondaryDarkBg
        )
    }
}

@Composable
private fun TripTabRow(
    selectedTab: TripTab,
    onTabSelected: (TripTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TripTabChip(
            label = "Upcoming Trips",
            icon = Icons.Outlined.CalendarMonth,
            selected = selectedTab == TripTab.UPCOMING,
            onClick = { onTabSelected(TripTab.UPCOMING) },
            modifier = Modifier.weight(1f)
        )
        TripTabChip(
            label = "Past Trips",
            icon = Icons.Outlined.History,
            selected = selectedTab == TripTab.PAST,
            onClick = { onTabSelected(TripTab.PAST) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TripTabChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Gold500 else Color(0x332E9A57)
    val textColor = if (selected) TextPrimaryDarkBg else TextSecondaryDarkBg.copy(alpha = 0.75f)
    val iconTint = if (selected) Gold500 else TextSecondaryDarkBg.copy(alpha = 0.65f)
    val backgroundColor = if (selected) Color(0x331B5E3E) else Color(0x22102018)

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontFamily = Poppins,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 13.sp,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TripBookingCard(
    booking: Booking,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = RafiqWelcomeCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, RafiqWelcomeCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.hexagon),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = booking.umrahPackage.title,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextPrimaryDarkBg,
                        modifier = Modifier.weight(1f)
                    )
                    TripStatusBadge(status = booking.status)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Gold500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${formatTripDate(booking.travelDate)} -> ${formatTripDate(booking.returnDate)}",
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = TextSecondaryDarkBg
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = null,
                        tint = Gold500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatTravelerCount(booking.numberOfTravelers),
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = TextSecondaryDarkBg
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatIndianPrice(booking.totalAmount),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AlSafarGreenLight
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Gold500,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun TripStatusBadge(status: String) {
    val label = status.replaceFirstChar { it.uppercase() }
    val isConfirmed = status.equals("confirmed", ignoreCase = true)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF0E221C),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isConfirmed) Color(0x442E9A57) else Color(0x33D4AF37)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isConfirmed) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E9A57)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
            Text(
                text = label,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = Gold500
            )
        }
    }
}

@Composable
private fun TripEmptyState(selectedTab: TripTab) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (selectedTab == TripTab.UPCOMING) {
                    "No upcoming trips"
                } else {
                    "No past trips"
                },
                fontFamily = Playfair,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = TextPrimaryDarkBg
            )
            Text(
                text = if (selectedTab == TripTab.UPCOMING) {
                    "Book an Umrah package to get started"
                } else {
                    "Your completed journeys will appear here"
                },
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = TextSecondaryDarkBg
            )
        }
    }
}

private fun filterBookings(bookings: List<Booking>, tab: TripTab): List<Booking> {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    return bookings.filter { booking ->
        val returnDate = runCatching { dateParser.parse(booking.returnDate) }.getOrNull()
        val isUpcoming = returnDate?.let { !it.before(today) } ?: true
        when (tab) {
            TripTab.UPCOMING -> isUpcoming
            TripTab.PAST -> !isUpcoming
        }
    }.sortedByDescending { booking ->
        runCatching {
            dateParser.parse(
                if (tab == TripTab.UPCOMING) booking.travelDate else booking.returnDate
            )?.time
        }.getOrNull() ?: 0L
    }
}

private fun formatTripDate(date: String): String {
    return runCatching {
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date) ?: return date
        SimpleDateFormat("dd MMM yyyy", Locale.US).format(input)
    }.getOrElse { date }
}

private fun formatTravelerCount(count: Int): String {
    return if (count == 1) "1 Traveler" else "$count Travelers"
}

private fun formatIndianPrice(amount: String): String {
    val value = amount.toDoubleOrNull() ?: return "₹$amount"
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return "₹${formatter.format(value)}"
}
