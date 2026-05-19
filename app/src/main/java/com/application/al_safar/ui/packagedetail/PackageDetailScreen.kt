package com.application.al_safar.ui.packagedetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.application.al_safar.R
import com.application.al_safar.domain.model.Package
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.CardLight
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextPrimaryLightCard
import com.application.al_safar.ui.theme.TextSecondaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryLightCard
import com.application.al_safar.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageDetailScreen(
    packageId: String,
    navController: NavController,
    viewModel: PackageDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(packageId) {
        viewModel.loadPackage(packageId)
    }

    Scaffold(
        containerColor = CardLight,
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Package Details",
                    style = Typography.headlineMedium,
                    color = TextPrimaryDarkBg
                )
                        },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimaryDarkBg)
                    }
                },
                actions = {
                    IconButton(onClick = {/*Add share option later when available */}) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = TextPrimaryDarkBg
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Emerald900
                )
            )
        },
        bottomBar = {
            if (uiState is PackageDetailUiState.Success) {
                BookNowBar(
                    onBookNow = {
                        navController.navigate(Screen.BookingDateGroup.createRoute(packageId))
                    }
                )
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is PackageDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Emerald700)
                }
            }

            is PackageDetailUiState.Success -> {
                PackageDetailContent(
                    pkg = state.umrahPackage,
                    modifier = Modifier.padding(padding)
                )
            }

            is PackageDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding).padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error, style = Typography.bodyLarge, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun PackageDetailContent(pkg: Package, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CardLight)
            .verticalScroll(rememberScrollState())
    ) {
        PackageHeroSection(pkg = pkg)

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PackageIncludesCard(pkg = pkg)
            PackageInfoCard(pkg = pkg)
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun PackageHeroSection(pkg: Package) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Emerald900).padding(start = 24.dp, top = 18.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(250.dp).padding(start = 24.dp, end = 24.dp, top = 18.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            AsyncImage(
                model = pkg.imageUrl,
                contentDescription = pkg.title,
                placeholder = painterResource(id = R.drawable.kaaba_background),
                error = painterResource(id = R.drawable.kaaba_background),
                fallback = painterResource(id = R.drawable.kaaba_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        PackageSummaryCard(
            pkg = pkg)
    }
}

@Composable
private fun PackageSummaryCard(
    pkg: Package,
    modifier: Modifier = Modifier
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pkg.packageType.displayTitle(),
                    style = Typography.headlineMedium,
                    color = TextPrimaryLightCard
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Umrah Package",
                    style = Typography.bodyLarge,
                    color = TextSecondaryLightCard
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatRupees(pkg.price),
                    style = Typography.titleLarge,
                    color = Emerald700,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Per Person",
                    style = Typography.bodySmall,
                    color = TextSecondaryLightCard
                )
            }
        }
    }
}

@Composable
private fun PackageIncludesCard(pkg: Package){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Package Includes",
                style = Typography.titleLarge,
                color = TextPrimaryLightCard
            )
            packageIncludes(pkg).forEach {item ->
                IncludeRow(text = item)
            }
        }
    }
}

@Composable
private fun IncludeRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier.size(34.dp).clip(RoundedCornerShape(17.dp)).background(Emerald700.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Emerald700,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = text,
            style = Typography.bodyLarge,
            color = TextPrimaryLightCard,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PackageInfoCard(pkg: Package){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Package Details",
                style = Typography.titleLarge,
                color = TextPrimaryLightCard
            )
            InfoRow(label = "Duration", value = "${pkg.durationDays} days")
            InfoRow(label = "Package Type", value = pkg.packageType.displayTitle())

            pkg.hotelName?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "Hotel", value = it)
            }

            pkg.distanceFromHaram?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "Distance from Haram", value = it)
            }

            pkg.hotelRating?.takeIf { it > 0 }?.let { rating ->
                RatingRow(rating = rating)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryLightCard,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimaryLightCard,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RatingRow(rating: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hotel Rating",
            style = Typography.bodyMedium,
            color = TextSecondaryLightCard,
            modifier = Modifier.weight(1f)
        )
        Row(modifier = Modifier.weight(1f)) {
            repeat(rating.coerceIn(0, 5)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Gold500,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun BookNowBar(onBookNow: () -> Unit){
    Box(
        modifier = Modifier.fillMaxWidth().background(CardLight).padding(horizontal = 24.dp, vertical = 18.dp)
    ){
        Button(
            onClick = onBookNow,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Emerald700,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Text(
                text = "Book Now",
                style = Typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun packageIncludes(pkg: Package): List<String> {
 val parsedIncludes = pkg.includes
     .split(",", "\n", ";")
     .map { it.trim() }
     .filter { it.isNotEmpty() }

    if (parsedIncludes.isNotEmpty()) {
        return parsedIncludes
    }
    return listOf(
        "Visa Processing",
        "Flight Tickets",
        "Hotel Stay (${pkg.durationDays} Days",
        "Ziyarat Tours",
        "Transport (AC)"
    )
}

@Composable
private fun formatRupees(value: String): String {
 val numericValue = value.toDoubleOrNull()
    return if (numericValue != null) {
        val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
        "₹${formatter.format(numericValue)}"
    } else {
        "₹$value"
    }
}

@Composable
private fun String.displayTitle(): String {
    return lowercase()
        .replace("_", " ")
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") {word ->
            word.replaceFirstChar { char -> char.uppercase() }
        }
}
