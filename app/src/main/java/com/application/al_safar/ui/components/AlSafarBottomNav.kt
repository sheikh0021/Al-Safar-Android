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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
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
import com.application.al_safar.ui.theme.Emerald700


@Composable
fun AlSafarBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
){

    Surface(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp).fillMaxWidth().height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Home",
                icon = if (currentRoute == "home") Icons.Default.Home else Icons.Outlined.Home,
                isSelected = currentRoute == "home"
            ){ onNavigate("home")}

            NavItem(
                label = "Bookings",
                icon = Icons.Default.Assignment,
                isSelected = currentRoute == "bookings"
            ) {onNavigate("bookings") }

            NavItem(
                label = "Support",
                icon = Icons.Default.QuestionAnswer,
                isSelected = currentRoute == "support"
            ) { onNavigate("support")}

            NavItem(
                label = "Profile",
                icon = Icons.Default.PersonOutline,
                isSelected = currentRoute == "profile"
            ) {onNavigate("profile") }
        }
    }

}

@Composable
private fun NavItem(
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
            contentDescription = label,
            tint = if (isSelected) Emerald700 else Color(0xFF7D848D),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) Emerald700 else Color(0xFF7D848D),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}