package com.application.al_safar.ui.chatbot


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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.R
import com.application.al_safar.domain.model.ChatMessage
import com.application.al_safar.ui.navigation.Screen
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.Playfair
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.RafiqBgBottom
import com.application.al_safar.ui.theme.RafiqBgTop
import com.application.al_safar.ui.theme.RafiqBotBubble
import com.application.al_safar.ui.theme.RafiqBotBubbleBorder
import com.application.al_safar.ui.theme.RafiqChipBorder
import com.application.al_safar.ui.theme.RafiqHeaderScrim
import com.application.al_safar.ui.theme.RafiqInputBg
import com.application.al_safar.ui.theme.RafiqInputBorder
import com.application.al_safar.ui.theme.RafiqKeywordGold
import com.application.al_safar.ui.theme.RafiqOnlineGreen
import com.application.al_safar.ui.theme.RafiqUserBubble
import com.application.al_safar.ui.theme.RafiqWelcomeCardBg
import com.application.al_safar.ui.theme.RafiqWelcomeCardBorder
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AiChatbotScreen(
    navController: NavController,
    viewModel: AiChatbotViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isLoading) {
        val lastIndex = listState.layoutInfo.totalItemsCount - 1
        if (lastIndex >= 0) listState.animateScrollToItem(lastIndex)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        RafiqScreenBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
        ) {
            RafiqTopBar(
                onBack = { navController.popBackStack() },
                onHistoryClick = { /* TODO: chat history screen */ }
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    RafiqWelcomeCard()
                }

                item {
                    ChatDateSeparator(label = "Today")
                }

                items(messages) { message ->
                    if (message.role == "user") {
                        RafiqUserBubble(message = message)
                    } else {
                        RafiqAssistantBubble(message = message)
                    }
                }

                if (isLoading) {
                    item { RafiqTypingIndicator() }
                }

                if (error != null) {
                    item {
                        RafiqErrorBanner(
                            message = error!!,
                            onDismiss = { viewModel.clearError() }
                        )
                    }
                }
            }

            RafiqQuickActionsRow(
                onUmrahPackages = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AiChatbot.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onDuas = {
                    navController.navigate(Screen.DuaCollection.route)
                },
                onPackingList = {
                    viewModel.sendQuickPrompt(
                        "Give me a complete Umrah packing list for a 10-day trip."
                    )
                }
            )

            RafiqInputBar(
                value = inputText,
                onValueChange = { inputText = it },
                isLoading = isLoading,
                onSend = {
                    if (!isLoading && inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText.trim())
                        inputText = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun RafiqScreenBackground() {
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
            painter = painterResource(id = R.drawable.qibla_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
            alpha = 0.9f
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            RafiqHeaderScrim,
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
private fun RafiqTopBar(
    onBack: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimaryDarkBg
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Rafiq",
                fontFamily = Playfair,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextPrimaryDarkBg
            )
            Text(
                text = "AI Umrah Assistant",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Gold500
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(RafiqOnlineGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Online",
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = TextSecondaryDarkBg
                )
            }
        }

        IconButton(onClick = onHistoryClick) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x331B5E3E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "Chat history",
                    tint = TextPrimaryDarkBg,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun RafiqWelcomeCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = RafiqWelcomeCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, RafiqWelcomeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.hexagon),
                contentDescription = "Rafiq",
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "As-salamu alaykum! I'm Rafiq",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextPrimaryDarkBg
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ask me anything about Umrah - rituals, duas, packages, or travel tips.",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = TextSecondaryDarkBg
                )
            }
        }
    }
}

@Composable
private fun ChatDateSeparator(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TextSecondaryDarkBg.copy(alpha = 0.35f))
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF1A2E28),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x332E9A57)),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = TextSecondaryDarkBg
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TextSecondaryDarkBg.copy(alpha = 0.35f))
        )
    }
}

@Composable
private fun RafiqUserBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp),
            color = RafiqUserBubble
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = TextPrimaryDarkBg
            )
        }
        Row(
            modifier = Modifier.padding(top = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatChatTime(message.timestampMillis),
                fontFamily = Poppins,
                fontSize = 11.sp,
                color = TextSecondaryDarkBg
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Read",
                tint = RafiqOnlineGreen,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun RafiqAssistantBubble(message: ChatMessage) {
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, Gold500, CircleShape)
                .background(RafiqBotBubble)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.kaaba),
                contentDescription = "Rafiq",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.widthIn(max = 300.dp)) {
            Surface(
                shape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp),
                color = RafiqBotBubble,
                border = androidx.compose.foundation.BorderStroke(1.dp, RafiqBotBubbleBorder)
            ) {
                Text(
                    text = highlightUmrahKeywords(message.content),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = TextPrimaryDarkBg,
                    lineHeight = 20.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatChatTime(message.timestampMillis),
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = TextSecondaryDarkBg
                )
                Row {
                    IconButton(
                        onClick = { clipboard.setText(AnnotatedString(message.content)) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy",
                            tint = Gold500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* TODO: feedback API */ },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "Helpful",
                            tint = Gold500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RafiqQuickActionsRow(
    onUmrahPackages: () -> Unit,
    onDuas: () -> Unit,
    onPackingList: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            RafiqQuickChip(
                label = "Umrah Packages",
                icon = Icons.Default.CardGiftcard,
                onClick = onUmrahPackages
            )
        }
        item {
            RafiqQuickChip(
                label = "Duas for Umrah",
                icon = Icons.Default.MenuBook,
                onClick = onDuas
            )
        }
        item {
            RafiqQuickChip(
                label = "Packing List",
                icon = Icons.Outlined.Luggage,
                onClick = onPackingList
            )
        }
    }
}

@Composable
private fun RafiqQuickChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, RafiqChipBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Gold500,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = TextPrimaryDarkBg,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RafiqInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(RafiqInputBg)
                .border(1.dp, RafiqInputBorder, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = TextPrimaryDarkBg,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                ),
                singleLine = false,
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Ask Rafiq anything about Umrah...",
                            color = TextSecondaryDarkBg,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    }
                    inner()
                }
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = onSend,
            enabled = !isLoading && value.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(RafiqUserBubble)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Gold500
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Send",
                    tint = Gold500
                )
            }
        }
    }
}

@Composable
private fun RafiqTypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.kaaba),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = RafiqBotBubble
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rafiq is typing",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = TextSecondaryDarkBg
                )
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = Gold500
                )
            }
        }
    }
}

@Composable
private fun RafiqErrorBanner(message: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0x33B3261E),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x55B3261E))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = TextPrimaryDarkBg
            )
            Text(
                text = "Dismiss",
                modifier = Modifier.clickable(onClick = onDismiss),
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Gold500
            )
        }
    }
}

private fun formatChatTime(timestampMillis: Long): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestampMillis))
}

private fun highlightUmrahKeywords(text: String): AnnotatedString {
    val keywords = listOf(
        "Ihram", "Tawaf", "Sa'i", "Sa'i", "Umrah", "Hajj", "Mina", "Arafat",
        "Sahih Bukhari", "Sahih Muslim"
    )
    return buildAnnotatedString {
        var remaining = text
        while (remaining.isNotEmpty()) {
            val match = keywords.mapNotNull { key ->
                val idx = remaining.indexOf(key, ignoreCase = true)
                if (idx >= 0) idx to key else null
            }.minByOrNull { it.first }

            if (match == null) {
                append(remaining)
                break
            }
            val (index, keyword) = match
            append(remaining.substring(0, index))
            withStyle(SpanStyle(color = RafiqKeywordGold, fontWeight = FontWeight.SemiBold)) {
                append(remaining.substring(index, index + keyword.length))
            }
            remaining = remaining.substring(index + keyword.length)
        }
    }
}