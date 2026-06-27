package com.application.al_safar.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.application.al_safar.R
import com.application.al_safar.domain.model.Package
import com.application.al_safar.ui.components.AlSafarBottomNav
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.notifications.NotificationViewModel
import com.application.al_safar.ui.theme.CardLight
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextPrimaryLightCard
import com.application.al_safar.ui.theme.TextSecondaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryLightCard
import com.application.al_safar.ui.theme.Typography
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onNavigateToUmrahGuide: () -> Unit,
    onNavigateToTripDashboard: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAiChatbot: () -> Unit,
    onNavigateToMyGuide: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val travelerName by viewModel.travelerName.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val logoutComplete by viewModel.logoutComplete.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    LaunchedEffect(logoutComplete) {
        if (logoutComplete){
            onLogout()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CardLight,
        bottomBar = {
            AlSafarBottomNav(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {saveState = true}
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {  innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
        ){
            when (val state = uiState) {
                is  HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = Emerald700)
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center){
                        Text(text = state.message, color = TextPrimaryLightCard, style = Typography.bodyLarge, textAlign = TextAlign.Center)
                    }
                }
                is HomeUiState.Success -> {
                    HomeContent(
                        navController = navController,
                        travelerName = travelerName,
                        unreadCount = unreadCount,
                        selectedFilter = selectedFilter,
                        packages = state.packages,
                        onFilterSelected = { type ->
                            selectedFilter = type
                            viewModel.loadPackages(type)
                        },
                        bottomContentPadding = innerPadding.calculateBottomPadding()
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    navController: NavController,
    travelerName: String,
    unreadCount: Int,
    selectedFilter: String?,
    packages: List<Package>,
    onFilterSelected: (String?) -> Unit,
    bottomContentPadding: Dp = 0.dp
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 110.dp + bottomContentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Hero & Intro Card Section
        item(span = { GridItemSpan(maxLineSpan) }){
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background Hero Image
                HomeHeroSection(
                    travelerName = travelerName,
                    unreadCount = unreadCount,
                    onBellClick = { navController.navigate(Screen.NotificationCenter.route) }
                )
                // Floating Card pushed down using a Spacer
                Column {
                    Spacer(modifier = Modifier.height(230.dp))
                    IntroFloatingCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // 2. Filter Row
        item(span = { GridItemSpan(maxLineSpan)}){
            Spacer(modifier = Modifier.height(16.dp))
            HomeFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected
            )
        }

        if (packages.isEmpty()){
            item(span = { GridItemSpan(maxLineSpan)}){
                EmptyPackagesCard()
            }
        }else {
            itemsIndexed(
                items = packages,
                span = {index, _ ->
                    if (packages.size % 2 == 1 && index == packages.lastIndex){
                        GridItemSpan(maxLineSpan)
                    }else {
                        GridItemSpan(1)
                    }
                }
            ) {_, pkg ->
                PremiumPackageCard(
                    pkg = pkg,
                    isWide = packages.size % 2 == 1 && pkg == packages.last(),
                    onClick = {
                        navController.navigate(Screen.PackageDetail.createRoute(pkg.id.toString()))
                    }
                )

            }
        }
    }
}

@Composable
private fun HomeHeroSection(
    travelerName: String,
    unreadCount: Int,
    onBellClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(288.dp)
            .background(Emerald900)
    ) {
        Image(
            painter = painterResource(id = R.drawable.qibla_bg),
            contentDescription = "Umrah hero background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Emerald900.copy(alpha = 0.18f),
                            Emerald900.copy(alpha = 0.32f),
                            Emerald900.copy(alpha = 0.78f),
                        )
                    )
                )
        )
        // Extra top scrim so name + bell stay readable over the photo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "As-salamu Alaykum",
                    style = Typography.bodyMedium,
                    color = TextSecondaryDarkBg
                )
                Text(
                    text = travelerName.ifBlank { "Traveler" },
                    style = Typography.headlineLarge,
                    color = TextPrimaryDarkBg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onBellClick) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(containerColor = Gold500)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = Gold500,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IntroFloatingCard(modifier: Modifier = Modifier){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(168.dp)
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 18.dp, top = 18.dp, end = 120.dp, bottom = 18.dp)
            ) {
                Text(
                    text = "Begin your \nUmrah journey\nwith peace of mind.",
                    style = Typography.headlineMedium,
                    color = Emerald700
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Choose the perfect package\n that suits your needs.",
                    style = Typography.bodyMedium,
                    color = TextSecondaryLightCard
                )
            }
            Image(
                painter = painterResource(id = R.drawable.faint_tomb),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(118.dp),
                alpha = 0.22f
            )
        }
    }
}

@Composable
private fun HomeFilterRow(
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HomeFilterChip(
            label = "All",
            selected = selectedFilter == null,
            onClick = {onFilterSelected(null)},
            modifier = Modifier.weight(1f)
        )
        HomeFilterChip(
            label = "basic",
            selected = selectedFilter == "basic",
            onClick = {onFilterSelected("basic")},
            modifier = Modifier.weight(1f)
        )
        HomeFilterChip(
            label = "standard",
            selected = selectedFilter == "standard",
            onClick = {onFilterSelected("standard")},
            modifier = Modifier.weight(1f)
        )
        HomeFilterChip(
            label = "premium",
            selected = selectedFilter == "premium",
            onClick = {onFilterSelected("premium")},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HomeFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Emerald700 else Color.White,
        tonalElevation = if (selected) 0.dp else 1.dp,
        shadowElevation = if (selected) 0.dp else 1.dp
    ) {
        Box(contentAlignment = Alignment.Center){
            Text(
                text = label,
                style = Typography.labelLarge,
                color = if (selected) Color.White else TextSecondaryLightCard
            )
        }
    }
}

@Composable
private fun PremiumPackageCard(
    pkg: Package,
    isWide: Boolean,
    onClick: () -> Unit
) {
    val titleText = if (pkg.title.isNotBlank()) {
        pkg.title
    } else {
        pkg.packageType.replaceFirstChar { it.uppercase() }
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isWide) Modifier.height(150.dp)
                else Modifier.aspectRatio(0.85f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.package_cards),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x55000000),
                                Color(0x88000000),
                                Color(0xCC0F3D2E)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = titleText,
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Umrah Package",
                        style = Typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }

                Column {
                    Text(
                        text = "₹${pkg.price}",
                        style = Typography.titleLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Gold500
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${pkg.durationDays} days",
                            style = Typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Emerald700)
                                .clickable(onClick = onClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "View Package",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPackagesCard(){
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No packages available",
                style = Typography.titleMedium,
                color = TextPrimaryLightCard
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try another filter to view more options.",
                style = Typography.bodyMedium,
                color = TextSecondaryLightCard,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PackageCard(pkg: Package, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pkg.packageType.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = pkg.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${pkg.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${pkg.durationDays} days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
