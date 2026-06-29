package com.application.al_safar.ui.guide

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.application.al_safar.R
import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.GuideChatBg
import com.application.al_safar.ui.theme.GuideChatHeaderDivider
import com.application.al_safar.ui.theme.GuideChatIncomingBubble
import com.application.al_safar.ui.theme.GuideChatInputBg
import com.application.al_safar.ui.theme.GuideChatOutgoingBubble
import com.application.al_safar.ui.theme.Poppins
import com.application.al_safar.ui.theme.RafiqBgBottom
import com.application.al_safar.ui.theme.RafiqBgTop
import com.application.al_safar.ui.theme.RafiqBotBubbleBorder
import com.application.al_safar.ui.theme.RafiqOnlineGreen
import com.application.al_safar.ui.theme.TextPrimaryDarkBg
import com.application.al_safar.ui.theme.TextSecondaryDarkBg

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuideChatScreen(
    assignmentId: String,
    isGuide: Boolean,
    myUserId: Int,
    otherPersonName: String,
    navController: NavController,
    viewModel: GuideChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val idInt = assignmentId.toIntOrNull() ?: 0

    LaunchedEffect(Unit) {
        viewModel.loadMessages(idInt, isGuide)
    }

    val messages = (uiState as? GuideChatUiState.Success)?.messages ?: emptyList()
    LaunchedEffect(messages.size, isSending) {
        val lastIndex = listState.layoutInfo.totalItemsCount - 1
        if (lastIndex >= 0) listState.animateScrollToItem(lastIndex)
    }

    fun send() {
        if (!isSending && inputText.isNotBlank()) {
            viewModel.sendMessage(idInt, inputText.trim(), isGuide)
            inputText = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(RafiqBgTop, RafiqBgBottom)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
        ) {
            GuideChatHeader(
                otherPersonName = otherPersonName,
                onBack = { navController.popBackStack() },
                onCallClick = { },
                onMenuClick = { }
            )

            when (val state = uiState) {
                is GuideChatUiState.Loading -> {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Gold500)
                    }
                }
                is GuideChatUiState.Error -> {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            fontFamily = Poppins,
                            color = Color(0xFFFF8A80),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is GuideChatUiState.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            GuideChatDateSeparator(label = "Today")
                        }

                        if (state.messages.isEmpty()) {
                            item {
                                Text(
                                    text = "No messages yet. Say Assalamu Alaikum!",
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    color = TextSecondaryDarkBg,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        items(state.messages, key = { it.id }) { message ->
                            val isFromMe = message.senderId == myUserId
                            if (isFromMe) {
                                GuideOutgoingBubble(
                                    message = message,
                                    timeText = formatGuideMessageTime(message.sentAt)
                                )
                            } else {
                                GuideIncomingBubble(
                                    message = message,
                                    timeText = formatGuideMessageTime(message.sentAt)
                                )
                            }
                        }

                        if (isSending) {
                            item {
                                GuideTypingIndicator()
                            }
                        }
                    }
                }
            }

            GuideChatInputBar(
                value = inputText,
                onValueChange = { inputText = it },
                isSending = isSending,
                onSend = { send() },
                onAttachClick = { }
            )
        }
    }
}

@Composable
private fun GuideChatHeader(
    otherPersonName: String,
    onBack: () -> Unit,
    onCallClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(GuideChatBg.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Gold500
                )
            }

            Image(
                painter = painterResource(id = R.drawable.hexagon),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = otherPersonName,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextPrimaryDarkBg
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        color = RafiqOnlineGreen
                    )
                }
            }

            IconButton(onClick = onCallClick) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    tint = Gold500,
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Gold500
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GuideChatHeaderDivider)
        )
    }
}

@Composable
private fun GuideChatDateSeparator(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TextSecondaryDarkBg.copy(alpha = 0.25f))
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF142820),
            border = BorderStroke(1.dp, RafiqBotBubbleBorder)
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
                .background(TextSecondaryDarkBg.copy(alpha = 0.25f))
        )
    }
}

@Composable
private fun GuideIncomingBubble(message: GuideMessage, timeText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.hexagon),
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.widthIn(max = 280.dp)) {
            Surface(
                shape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp),
                color = GuideChatIncomingBubble,
                border = BorderStroke(1.dp, RafiqBotBubbleBorder)
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextPrimaryDarkBg
                )
            }
            Text(
                text = timeText,
                fontFamily = Poppins,
                fontSize = 11.sp,
                color = Gold500.copy(alpha = 0.85f),
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun GuideOutgoingBubble(message: GuideMessage, timeText: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp),
            color = GuideChatOutgoingBubble
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.content,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextPrimaryDarkBg
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeText,
                        fontFamily = Poppins,
                        fontSize = 11.sp,
                        color = Gold500.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Read",
                        tint = if (message.isRead) Gold500 else TextSecondaryDarkBg,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideTypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.hexagon),
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GuideChatIncomingBubble
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == 2) Gold500 else TextSecondaryDarkBg.copy(alpha = 0.6f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    isSending: Boolean,
    onSend: () -> Unit,
    onAttachClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideChatBg.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onAttachClick,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(1.dp, Gold500, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Attach",
                tint = Gold500
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GuideChatInputBg)
                .border(1.dp, Gold500.copy(alpha = 0.55f), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = TextPrimaryDarkBg,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                ),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Type your message...",
                            color = TextSecondaryDarkBg,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    }
                    inner()
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSend,
            enabled = !isSending && value.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(GuideChatOutgoingBubble)
        ) {
            if (isSending) {
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

private fun formatGuideMessageTime(sentAt: String): String {
    return runCatching {
        val utc = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
        val local = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US)
        val date = runCatching { utc.parse(sentAt) }.getOrNull()
            ?: runCatching { local.parse(sentAt.take(19)) }.getOrNull()
            ?: return sentAt
        java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US).format(date)
    }.getOrElse { sentAt }
}
