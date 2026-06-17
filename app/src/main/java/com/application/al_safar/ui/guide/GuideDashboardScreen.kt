package com.application.al_safar.ui.guide

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.ui.components.GuideBottomNav
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.ProfileCardDark
import com.application.al_safar.ui.theme.ProfileDeepDark
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg

@Composable
fun GuideDashboardScreen(
    navController: NavController,
    guideName: String,
    myUserId: Int,
    onLogout: () -> Unit,
    viewModel: GuideDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val logoutComplete by viewModel.logoutComplete.collectAsState()

    LaunchedEffect(logoutComplete) {
        if (logoutComplete) onLogout()
    }

    Scaffold(
        containerColor = ProfileDeepDark,
        bottomBar = {
            GuideBottomNav(
                selectedTab = selectedTab,
                unreadCount = viewModel.getTotalUnreadCount(),
                onTabSelected = { tab ->
                    if (tab is GuideTab.Profile) {
                        navController.navigate(Screen.GuideProfile.route)
                    } else {
                        viewModel.selectTab(tab)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GuideDashboardHeader(
                guideName = guideName,
                onRefresh = { viewModel.loadTravelers() }
            )

            when (selectedTab) {
                is GuideTab.Trips -> TripsTabContent(viewModel = viewModel)
                is GuideTab.Chats -> ChatsTabContent(
                    viewModel = viewModel,
                    onChatClick = { assignment ->
                        navController.navigate(
                            Screen.GuideChatDetail.createRoute(assignment.id.toString())
                        )
                    }
                )
                is GuideTab.Profile -> { }
            }
        }
    }
}

@Composable
private fun GuideDashboardHeader(
    guideName: String,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ProfileDeepDark)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(2.dp, Gold500, CircleShape)
                    .background(Emerald700),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Guide Avatar",
                    tint = Gold500,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "As-salamu Alaykum",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
                Text(
                    text = if (guideName.isNotBlank()) guideName else "Guide",
                    fontFamily = Playfair,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimaryDarkBg
                )
                Text(
                    text = "Local Guide · Al-Safar",
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Gold500
                )
            }

            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = TextSecondaryDarkBg
                )
            }
        }
    }
}

@Composable
private fun TripsTabContent(viewModel: GuideDashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val acceptState by viewModel.acceptState.collectAsState()
    val tripTabs = listOf("Ongoing", "Upcoming", "Completed", "New")
    var selectedTripTab by remember { mutableStateOf(0) }

    LaunchedEffect(acceptState) {
        when (acceptState) {
            is AcceptAssignmentState.Success -> viewModel.resetAcceptState()
            is AcceptAssignmentState.Error -> viewModel.resetAcceptState()
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTripTab,
            containerColor = ProfileDeepDark,
            contentColor = Gold500,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTripTab])
                        .height(2.dp)
                        .background(Gold500)
                )
            },
            divider = {}
        ) {
            tripTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTripTab == index,
                    onClick = { selectedTripTab = index },
                    text = {
                        Text(
                            text = title,
                            fontFamily = Poppins,
                            fontWeight = if (selectedTripTab == index) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (selectedTripTab == index) Gold500 else TextSecondaryDarkBg
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is GuideDashboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold500)
                }
            }
            is GuideDashboardUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (uiState as GuideDashboardUiState.Error).message,
                            fontFamily = Poppins,
                            color = TextSecondaryDarkBg,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadTravelers() },
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald700)
                        ) {
                            Text("Retry", fontFamily = Poppins, color = TextPrimaryDarkBg)
                        }
                    }
                }
            }
            is GuideDashboardUiState.Success -> {
                val trips = when (selectedTripTab) {
                    0    -> viewModel.getOngoingTrips()
                    1    -> viewModel.getUpcomingTrips()
                    2    -> viewModel.getCompletedTrips()
                    else -> viewModel.getPendingTrips()
                }
                val emptyMessage = when (selectedTripTab) {
                    0    -> "No ongoing trips right now."
                    1    -> "No upcoming trips scheduled."
                    else -> "No completed trips yet."
                }
                TripList(trips = trips, emptyMessage = emptyMessage, onAcceptClick = {assignmentId ->
                    viewModel.acceptAssignment(assignmentId)
                })
            }
        }
    }
}

@Composable
private fun TripList(trips: List<GuideAssignment>, emptyMessage: String, onAcceptClick: (Int) -> Unit) {
    if (trips.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emptyMessage, fontFamily = Poppins, color = TextSecondaryDarkBg, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips) { assignment ->
                TripStatusCard(assignment = assignment, onAcceptClick = onAcceptClick)
            }
        }
    }
}

@Composable
private fun TripStatusCard(assignment: GuideAssignment, onAcceptClick: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ProfileCardDark
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = assignment.travelerName ?: "Traveler",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextPrimaryDarkBg
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (assignment.status.lowercase()) {
                        "ongoing", "active"     -> Emerald700
                        "completed", "done"     -> Color(0xFF2D5A27)
                        "pending_acceptance"    -> Color(0xFF5A3A00)
                        else                    -> Color(0xFF4A3800)
                    }
                ) {
                    Text(
                        text = assignment.status.replaceFirstChar { it.uppercase() },
                        fontFamily = Poppins,
                        fontSize = 11.sp,
                        color = when (assignment.status.lowercase()) {
                            "ongoing", "active" -> TextPrimaryDarkBg
                            "completed", "done" -> Color(0xFF90EE90)
                            "pending_acceptance"-> Color(0xFFFFCC66)
                            else                -> Gold500
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Text(
                text = assignment.packageTitle ?: "Package N/A",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Gold500
            )
            if (!assignment.travelDate.isNullOrBlank()) {
                Text(
                    text = "Travel Date: ${assignment.travelDate}",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
            }
            if (!assignment.travelerPhotoUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Traveler Photo:", fontFamily = Poppins, fontSize = 12.sp , color = TextSecondaryDarkBg)
                AsyncImage(
                    model = assignment.travelerPhotoUrl,
                    contentDescription = "Traveler passport photo",
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Gold500, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            if (assignment.status.lowercase() == "pending_acceptance") {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {onAcceptClick(assignment.id)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald700
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Accept Booking",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDarkBg
                    )
                }
            }
            if (!assignment.travelerEmail.isNullOrBlank()) {
                Text(
                    text = assignment.travelerEmail,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
            }
        }
    }
}

@Composable
private fun ChatsTabContent(
    viewModel: GuideDashboardViewModel,
    onChatClick: (GuideAssignment) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val assignments = (uiState as? GuideDashboardUiState.Success)?.assignments ?: emptyList()

    if (assignments.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No traveler chats yet.", fontFamily = Poppins, color = TextSecondaryDarkBg, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(assignments) { assignment ->
                ChatInboxCard(assignment = assignment, onClick = { onChatClick(assignment) })
            }
        }
    }
}

@Composable
private fun ChatInboxCard(assignment: GuideAssignment, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = ProfileCardDark
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(Emerald700),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = assignment.travelerName?.firstOrNull()?.uppercase() ?: "T",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Gold500
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.travelerName ?: "Traveler",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimaryDarkBg
                )
                Text(
                    text = assignment.packageTitle ?: "",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
            }
            if (assignment.unreadCount > 0) {
                Badge(containerColor = Gold500) {
                    Text(text = "${assignment.unreadCount}", color = Emerald900, fontSize = 11.sp)
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondaryDarkBg,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}