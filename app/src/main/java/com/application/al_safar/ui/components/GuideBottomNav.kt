package com.application.al_safar.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.al_safar.ui.guide.GuideTab
import com.application.al_safar.ui.theme.Emerald700


@Composable
fun GuideBottomNav(
    selectedTab: GuideTab,
    unreadCount: Int,
    onTabSelected : (GuideTab) -> Unit
) {
    Surface(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp ).fillMaxWidth().height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GuideNavItem(
                label = "Trips",
                icon = Icons.Default.DateRange,
                isSelected = selectedTab is GuideTab.Trips,
                onClick = {onTabSelected(GuideTab.Trips)}
            )
            GuideNavItemWithBadge(
                label = "Chats",
                icon = Icons.Default.Chat,
                isSelected = selectedTab is GuideTab.Chats,
                badgeCount = unreadCount,
                onClick = {onTabSelected(GuideTab.Chats)}
            )
            GuideNavItem(
                label = "Profile",
                icon = Icons.Default.Person,
                isSelected = selectedTab is GuideTab.Profile,
                onClick = {onTabSelected(GuideTab.Profile)}
            )
        }
    }
}

@Composable
private fun GuideNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Emerald700 else Color(0xFF7D848D),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Emerald700 else Color(0xFF7D848D),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun GuideNavItemWithBadge(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(8.dp)
    ) {
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(containerColor = Emerald700) {
                        Text(
                            text = if (badgeCount > 99) "99+" else "$badgeCount",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Emerald700 else Color(0xFF7d848D),
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Emerald700 else Color(0xFF7D848D),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}