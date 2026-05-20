package com.application.al_safar.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.al_safar.ui.theme.Emerald700
import com.application.al_safar.ui.theme.Gold500
import com.application.al_safar.ui.theme.TextSecondaryLightCard
import com.application.al_safar.ui.theme.Typography

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
    val canSubmit = username.isNotBlank() && password.length >= 6

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess((uiState as LoginUiState.Success).user.role)
        }
    }

    Scaffold(containerColor = Color.Transparent) { padding ->
        AuthScreenBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(44.dp))

                AuthBrandHeader(
                    title = "Al-Safar",
                    subtitle = "Your Journey, Our Care.",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(36.dp))

                AuthCard(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        style = Typography.headlineLarge,
                        color = Emerald700,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sign in to continue your spiritual journey",
                        style = Typography.bodyLarge,
                        color = TextSecondaryLightCard,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    AuthRoleSelector(
                        selectedRole = selectedRole,
                        onRoleSelected = { selectedRole = it }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    AuthTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = "Email or Phone Number",
                        leadingIcon = AuthEmailIcon,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        leadingIcon = AuthPasswordIcon,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            PasswordVisibilityButton(
                                visible = passwordVisible,
                                onToggle = { passwordVisible = !passwordVisible }
                            )
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = Typography.bodyMedium,
                            color = Emerald700,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    AuthPrimaryButton(
                        text = "Sign In",
                        isLoading = uiState is LoginUiState.Loading,
                        enabled = canSubmit,
                        onClick = { viewModel.login(username, password) }
                    )

                    if (uiState is LoginUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as LoginUiState.Error).message,
                            color = Color(0xFFB3261E),
                            style = Typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    AuthSocialRow()

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Don't have an account? ",
                            style = Typography.bodyMedium,
                            color = TextSecondaryLightCard
                        )
                        Text(
                            text = "Sign Up",
                            style = Typography.bodyMedium,
                            color = Emerald700,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToSignup() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Made with love by Sami",
                    style = Typography.titleMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}