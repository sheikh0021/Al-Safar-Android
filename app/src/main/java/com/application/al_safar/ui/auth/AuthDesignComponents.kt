package com.application.al_safar.ui.auth

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.application.al_safar.R
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Emerald900
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.TextPrimaryLightCard
import com.application.al_safar.ui.theme.TextSecondaryLightCard
import com.application.al_safar.ui.theme.Typography


@Composable
fun AuthScreenBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.kaaba_background),
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
                            Emerald900.copy(alpha = 0.92f),
                            Emerald900.copy(alpha = 0.72f),
                            Color(0xFFFAF8F1).copy(alpha = 0.96f),
                            Color(0xFFFAF8F1)
                        )
                    )
                )
        )

        content()
    }
}

@Composable
fun AuthBrandHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Al-Safar logo",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = Typography.headlineLarge,
            color = Emerald900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            style = Typography.bodyLarge,
            color = TextSecondaryLightCard,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp, bottomStart = 26.dp, bottomEnd = 26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCF7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = Typography.bodyMedium,
                color = TextSecondaryLightCard
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Emerald700
            )
        },
        trailingIcon = trailingIcon,
        singleLine = true,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Emerald700,
            unfocusedBorderColor = Color(0xFFC8D6CD),
            focusedTextColor = TextPrimaryLightCard,
            unfocusedTextColor = TextPrimaryLightCard,
            cursorColor = Emerald700,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun AuthPrimaryButton(
    text: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Emerald700,
            contentColor = Color.White,
            disabledContainerColor = Emerald700.copy(alpha = 0.45f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = null,
                tint = Gold500,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = text,
                style = Typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AuthRoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEAF2ED))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AuthRoleOption(
            label = "Traveler",
            selected = selectedRole == "traveler",
            onClick = { onRoleSelected("traveler") },
            modifier = Modifier.weight(1f)
        )
        AuthRoleOption(
            label = "Local Guide",
            selected = selectedRole == "local_guide",
            onClick = { onRoleSelected("local_guide") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthRoleOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(13.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Emerald700,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
        }
        Text(
            text = label,
            style = Typography.labelLarge,
            color = if (selected) Emerald700 else TextSecondaryLightCard
        )
    }
}

@Composable
fun AuthSocialRow() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(Color(0xFFD8DDD8))
            )
            Text(
                text = "or continue with",
                style = Typography.bodySmall,
                color = TextSecondaryLightCard,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(Color(0xFFD8DDD8))
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthSocialButton(text = "Google", modifier = Modifier.weight(1f))
            AuthSocialButton(text = "Apple", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun AuthSocialButton(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFD8DDD8), RoundedCornerShape(14.dp))
            .clickable { }
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = Typography.titleMedium,
            color = TextPrimaryLightCard
        )
    }
}

@Composable
fun PasswordVisibilityButton(
    visible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = if (visible) "Hide password" else "Show password",
            tint = Emerald700
        )
    }
}

val AuthEmailIcon = Icons.Default.Email
val AuthPasswordIcon = Icons.Default.Lock
val AuthPersonIcon = Icons.Default.Person
val AuthPhoneIcon = Icons.Default.Phone