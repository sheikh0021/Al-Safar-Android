package com.application.al_safar.ui.messages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.GuideChatIncomingBubble
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.ProfileCardDark
import com.application.al_safar.ui.theme.RafiqBgBottom
import com.application.al_safar.ui.theme.RafiqBgTop
import com.application.al_safar.ui.theme.RafiqBotBubbleBorder
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg


@Composable
fun TravelerMessagesScreen(
    navController: NavController,
    viewModel: TravelerMessagesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(RafiqBgTop,
                RafiqBgBottom
            )))
        )
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(
                    horizontal = 8.dp, vertical = 8.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Gold500
                    )
                }
                Column{
                    Text(
                        text = "Messages",
                        fontFamily = Playfair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = TextPrimaryDarkBg
                    )
                    Text(
                        text = "Chat with your local guide",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = TextSecondaryDarkBg
                    )
                }
            }
            when (val state = uiState) {
                is TravelerMessagesUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = Gold500)
                    }
                }
                is TravelerMessagesUiState.Error -> {
                    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color(0xFFFF8A80), fontFamily = Poppins)
                    }
                }
                is TravelerMessagesUiState.Success -> {
                    if (state.assignments.isEmpty()) {
                        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "No guide assigned yet",
                                    fontFamily = Playfair,
                                    fontSize = 18.sp,
                                    color = TextPrimaryDarkBg
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    "After you book and a guide is assigned, you can chat here.",
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    color = TextSecondaryDarkBg
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 20.dp,
                                vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.assignments, key = {it.id}) {assignment ->
                                TravelerInboxCard(
                                    assignment = assignment,
                                    onClick = {
                                        val guide = assignment.guide
                                        val guideName = if (guide != null) {
                                            "${guide.firstName} ${guide.lastName}".trim()
                                        } else {
                                            "Your Guide"
                                        }
                                        navController.navigate(
                                            Screen.TravelerChat.createRoute(
                                                assignmentId = assignment.id.toString(),
                                                guideName = guideName
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TravelerInboxCard(assignment: GuideAssignment, onClick: () -> Unit){
    val guide = assignment.guide
    val guideName = if (guide != null) {
        "${guide.firstName} ${guide.lastName}".trim()
    } else {
        "Local Guide"
    }

    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = GuideChatIncomingBubble,
        border = BorderStroke(1.dp, RafiqBotBubbleBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.hexagon ),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = guideName,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimaryDarkBg
                )
                Text(
                    text = assignment.packageTitle ?: "Umrah Package",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
            }
            if (assignment.unreadCount > 0) {
                Badge(
                    containerColor = Gold500
                ){
                    Text("${assignment.unreadCount}", color = Emerald900, fontSize = 12.sp)
                }
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondaryDarkBg,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}