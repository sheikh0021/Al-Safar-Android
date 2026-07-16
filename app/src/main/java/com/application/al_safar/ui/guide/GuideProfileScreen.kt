package com.application.al_safar.ui.guide

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.domain.model.LocalGuide
import com.application.al_safar.domain.model.User
import com.application.al_safar.ui.components.GuideBottomNav
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.ProfileCardDark
import com.application.al_safar.ui.theme.ProfileDeepDark
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg

@Composable
fun GuideProfileScreen(
    navController: NavController,
    viewModel: GuideProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()

    Scaffold(
        containerColor = ProfileDeepDark,
        bottomBar = {
            GuideBottomNav(
                selectedTab = GuideTab.Profile,
                unreadCount = 0,
                onTabSelected = { tab ->
                    if (tab !is GuideTab.Profile) navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is GuideProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Gold500) }
            }
            is GuideProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, fontFamily = Poppins, color = TextSecondaryDarkBg)
                }
            }
            is GuideProfileUiState.Success -> {
                GuideProfileContent(
                    user = state.user,
                    guideDetails = state.guideDetails,
                    bottomPadding = paddingValues.calculateBottomPadding(),
                    isLoggingOut = isLoggingOut,
                    onLogout = { viewModel.logout(); onLogout() },
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
        }
    }
}

@Composable
private fun GuideProfileContent(
    user: User,
    guideDetails: LocalGuide?,
    bottomPadding: Dp,
    isLoggingOut: Boolean,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ProfileDeepDark)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Profile", fontFamily = Playfair, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = TextPrimaryDarkBg)
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondaryDarkBg)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(80.dp)) {
                        Box(
                            modifier = Modifier.size(76.dp).clip(CircleShape).border(2.dp, Gold500, CircleShape).background(ProfileDeepDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Avatar", tint = Gold500, modifier = Modifier.size(44.dp))
                        }
                        Box(
                            modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).clip(CircleShape).background(Gold500).clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = ProfileDeepDark, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "${user.firstName} ${user.lastName}", fontFamily = Playfair, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextPrimaryDarkBg)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = user.email, fontFamily = Poppins, fontSize = 13.sp, color = TextSecondaryDarkBg)
                        user.phoneNumber?.takeIf { it.isNotBlank() }?.let { phone ->
                            Text(text = phone, fontFamily = Poppins, fontSize = 13.sp, color = TextSecondaryDarkBg)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            color = ProfileCardDark
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape).border(1.5.dp, Gold500, CircleShape).background(ProfileDeepDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Gold500, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Al-Safar Guide", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimaryDarkBg)
                    Text(
                        text = if (guideDetails?.isAvailable == true) "Available for trips" else "Currently unavailable",
                        fontFamily = Poppins, fontSize = 12.sp,
                        color = if (guideDetails?.isAvailable == true) Gold500 else TextSecondaryDarkBg
                    )
                }
                if (!guideDetails?.rating.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Gold500, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = guideDetails?.rating ?: "", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Gold500)
                    }
                }
            }
        }

        if (guideDetails != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                color = ProfileCardDark
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Guide Details", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextSecondaryDarkBg)
                    if (guideDetails.city.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Gold500, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = guideDetails.city, fontFamily = Poppins, fontSize = 13.sp, color = TextPrimaryDarkBg)
                        }
                    }
                    if (guideDetails.languagesList.isNotEmpty()) {
                        Text(text = "Languages", fontFamily = Poppins, fontSize = 12.sp, color = TextSecondaryDarkBg)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            guideDetails.languagesList.forEach { lang ->
                                Surface(shape = RoundedCornerShape(20.dp), color = Emerald700) {
                                    Text(text = lang, fontFamily = Poppins, fontSize = 11.sp, color = TextPrimaryDarkBg, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                }
                            }
                        }
                    }
                    if (!guideDetails.bio.isNullOrBlank()) {
                        Text(text = "About", fontFamily = Poppins, fontSize = 12.sp, color = TextSecondaryDarkBg)
                        Text(text = guideDetails.bio, fontFamily = Poppins, fontSize = 13.sp, color = TextPrimaryDarkBg)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(text = "Support", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimaryDarkBg, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), shape = RoundedCornerShape(16.dp), color = ProfileCardDark) {
            Column {
                GProfileItem(icon = Icons.Default.Notifications, label = "Notifications", onClick = { onNavigate(Screen.NotificationPreferences.route) })
                GDivider()
                GProfileItem(icon = Icons.Default.Help, label = "Help Center", onClick = { })
                GDivider()
                GProfileItem(icon = Icons.Default.SupportAgent, label = "Contact Admin", onClick = { onNavigate(Screen.AiChatbot.route) })
                GDivider()
                GProfileItem(icon = Icons.Default.Info, label = "About Al-Safar", onClick = { })
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(12.dp))
                .clickable(enabled = !isLoggingOut, onClick = onLogout).padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log out", tint = Color(0xFFE57373), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (isLoggingOut) "Logging out…" else "Log Out", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFFE57373))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GProfileItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextSecondaryDarkBg, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(text = label, fontFamily = Poppins, fontSize = 14.sp, color = TextPrimaryDarkBg, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondaryDarkBg, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun GDivider() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(Color.White.copy(alpha = 0.08f)))
}