package com.application.al_safar.ui.auth

import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.al_safar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (role: String) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("traveler") }

    val uiState by viewModel.uiState.collectAsState()
    val isValid = username.isNotEmpty() && password.length >= 6

    val buttonScale by animateFloatAsState(
        targetValue = if (isValid) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ButtonScale"
    )

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess((uiState as LoginUiState.Success).user.role)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Surface(
                onClick = {/* Back */},
                modifier = Modifier.padding(24.dp).size(45.dp),
                shape = CircleShape,
                color = Color(0xFFF7F7F9),
                tonalElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    ) {padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Sign in Now",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1E28)
            )
            Text(
                text = "Please sign in to continue ....",
                fontSize = 16.sp,
                color = Color(0xFF7D848D),
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            RoleSelector(
                selectedRole = selectedRole,
                onRoleSelected = {selectedRole = it}
            )
            Spacer(modifier = Modifier.height(30.dp))

            FancyTextField(
                value = username,
                onValueChange = {username = it},
                placeholder = "Username or Email here....",
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(20.dp))

            FancyTextField(
                value = password,
                onValueChange = {password = it},
                placeholder = "*********",
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = {passwordVisible = !passwordVisible}
            )

            Text(
                text = "Forget Password?",
                color = Color(0xFF0D6EFD),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.End).padding(top = 16.dp).clickable{}
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {viewModel.login(username, password)},
                modifier = Modifier.fillMaxWidth().height(56.dp).scale(buttonScale),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isValid) Color(0xFF0D6EFD) else Color(0xFF0D6EFD).copy(alpha = 0.6f),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isValid) 8.dp else 0.dp,
                    pressedElevation = 2.dp
                ),
                enabled = uiState !is LoginUiState.Loading
            ) {
                if (uiState is LoginUiState.Loading){
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                }else {
                    Text(text = "Sign In", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
Row {
    Text(
        "Dont have an Account?", color = Color(0xFF7D848D)
    )
    Text(
        "Sign Up",
        color = Color(0xFF0D6EFD),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable{onNavigateToSignup()}
    )
}
            Spacer(modifier = Modifier.height(24.dp))
            Text("Or Connect", color = Color(0xFF7D848D), fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SocialCircleButton(painterResource(id = R.drawable.ic_launcher_foreground))
                SocialCircleButton(painterResource(id = R.drawable.ic_launcher_foreground))
                SocialCircleButton(painterResource(id = R.drawable.ic_launcher_foreground))

            }


        }
    }
}

@Composable
fun RoleSelector(selectedRole: String, onRoleSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(12.dp)).background(Color(0XFFF7F7F9)).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RoleOption(
            label = "🕋Traveler",
            isSelected = selectedRole == "traveler",
            onClick = {onRoleSelected("traveler")},
            modifier = Modifier.weight(1f)
        )
        RoleOption(
            label = "🧭Guide",
            isSelected = selectedRole == "local_guide",
            onClick = {onRoleSelected("local_guide")},
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun RoleOption(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(durationMillis = 400)
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0D6EFD) else Color(0xFF7D848D),
        animationSpec = tween(durationMillis = 1000)
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, color = textColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
          }
    }
}

@Composable
fun FancyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text

){
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {Text(placeholder, color = Color(0xFF7D848D))},
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF7F7F9),
            unfocusedContainerColor = Color.Green,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF0D6EFD)
        ),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword){
                IconButton(onClick = onPasswordToggle) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF7D848D)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}


@Composable
fun SocialCircleButton(icon: Painter){
    Box(
        modifier = Modifier.size(60.dp).clip(CircleShape).clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = Color.Gray),
            onClick = {}
        ),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}