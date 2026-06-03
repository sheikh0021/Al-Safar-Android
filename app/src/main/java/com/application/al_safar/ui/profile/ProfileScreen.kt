package com.application.al_safar.ui.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.domain.model.User
import com.application.al_safar.ui.components.AlSafarBottomNav
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()

    // Derive the current back stack route to highlight the correct bottom nav tab
    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontFamily = Playfair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = TextPrimaryDarkBg
                    )
                },
                actions = {
                    IconButton(onClick = { /* navigate to settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondaryDarkBg
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Emerald900
                )
            )
        },
        bottomBar = {
            AlSafarBottomNav(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        },
        containerColor = Emerald900
    ) { paddingValues ->

        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold500)
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = TextSecondaryDarkBg,
                        fontFamily = Poppins
                    )
                }
            }

            is ProfileUiState.Success -> {
                ProfileContent(
                    user = state.user,
                    paddingValues = paddingValues,
                    isLoggingOut = isLoggingOut,
                    onLogout = {
                        viewModel.logout()
                        onLogout()
                    },
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    paddingValues: PaddingValues,
    isLoggingOut: Boolean,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.size(80.dp)) {
                    Box(
                        modifier = Modifier.size(76.dp).clip(CircleShape).border(2.dp, Gold500, CircleShape)
                            .background(Emerald700),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Gold500,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).clip(CircleShape).background(Gold500).clickable{},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit avatar",
                            tint = Emerald900,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column{
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontFamily = Playfair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextPrimaryDarkBg
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user.email,
                        fontFamily = Poppins,
                        fontSize = 13.sp,
                        color = TextSecondaryDarkBg
                    )
                    if (!user.phoneNumber.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user.phoneNumber,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            color = TextSecondaryDarkBg
                        )
                    }
                }
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable{},
            shape = RoundedCornerShape(16.dp),
            color = Emerald700
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape).border(1.5.dp, Gold500, CircleShape)
                        .background(Emerald900),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Gold500,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Al-Safar Member",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color=TextPrimaryDarkBg
                    )
                    Text(
                        text = "Member since May 2025",
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                        color = Gold500
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondaryDarkBg
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))

        ProfileSectionHeader(title = "My Account")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenuGroup {
            ProfileMenuItem(
                icon = Icons.Default.Person,
                label = "Personal Information",
                onClick = {}
            )
            ProfileMenuDivider()
            ProfileMenuItem(
              icon = Icons.Default.ShoppingBag,
                label = "My Bookings",
                onClick = {onNavigate(Screen.TripDashboard.route)}
            )
            ProfileMenuDivider()
            ProfileMenuItem(
              icon = Icons.Default.Wallet,
                label = "Notifications",
                onClick = {onNavigate(Screen.NotificationPreferences.route)}
            )
            ProfileMenuDivider()
            ProfileMenuItem(
              icon = Icons.Default.Lock,
                label = "Privacy & Security",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        ProfileSectionHeader(title = "Support")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenuGroup {
            ProfileMenuItem(
                icon = Icons.Default.Help,
                label = "Help Center",
                onClick = {}
            )
            ProfileMenuDivider()
            ProfileMenuItem(
              icon = Icons.Default.SupportAgent,
                label = "Contact Us",
                onClick = {onNavigate(Screen.AiChatbot.route)}
            )
            ProfileMenuDivider()
            ProfileMenuItem(
              icon = Icons.Default.Info,
                label = "About Al-Safar",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        // ── Log Out ──────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = !isLoggingOut, onClick = onLogout)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Log out",
                tint = Color(0xFFE57373),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLoggingOut) "Logging out…" else "Log Out",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFFE57373)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = TextPrimaryDarkBg,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun ProfileMenuGroup(content: @Composable () -> Unit){
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        color = Emerald700
    ) {
        Column { content() }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label:  String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondaryDarkBg,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = TextPrimaryDarkBg,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondaryDarkBg,
            modifier = Modifier.size(20.dp)
        )
    }
}


@Composable
private fun ProfileMenuDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(0.5.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}