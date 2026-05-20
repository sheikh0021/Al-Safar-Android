package com.application.al_safar.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.application.al_safar.ui.theme.TextSecondaryLightCard
import com.application.al_safar.ui.theme.Typography

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    onSignupSuccess: (role: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("traveler") }

    val uiState by viewModel.uiState.collectAsState()
    val passwordsMatch = confirmPassword.isBlank() || password == confirmPassword
    val canSubmit = email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword &&
            firstName.isNotBlank() &&
            lastName.isNotBlank()

    LaunchedEffect(uiState) {
        if (uiState is SignupUiState.Success) {
            val role = (uiState as SignupUiState.Success).user.role
            onSignupSuccess(role)
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
                Spacer(modifier = Modifier.height(34.dp))

                AuthBrandHeader(
                    title = "Al-Safar",
                    subtitle = "Begin your journey with care.",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                AuthCard(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Create Account",
                        style = Typography.headlineLarge,
                        color = Emerald700,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Join Al-Safar and plan your spiritual journey",
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
                        value = firstName,
                        onValueChange = { firstName = it },
                        placeholder = "First Name",
                        leadingIcon = AuthPersonIcon
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        placeholder = "Last Name",
                        leadingIcon = AuthPersonIcon
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email Address",
                        leadingIcon = AuthEmailIcon,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = "Phone Number Optional",
                        leadingIcon = AuthPhoneIcon,
                        keyboardType = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirm Password",
                        leadingIcon = AuthPasswordIcon,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (confirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            PasswordVisibilityButton(
                                visible = confirmPasswordVisible,
                                onToggle = { confirmPasswordVisible = !confirmPasswordVisible }
                            )
                        },
                        isError = !passwordsMatch,
                        supportingText = {
                            if (!passwordsMatch) {
                                Text("Passwords do not match")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    AuthPrimaryButton(
                        text = "Create Account",
                        isLoading = uiState is SignupUiState.Loading,
                        enabled = canSubmit,
                        onClick = {
                            viewModel.signup(
                                email = email,
                                password = password,
                                firstName = firstName,
                                lastName = lastName,
                                phoneNumber = phoneNumber.ifBlank { null },
                                role = selectedRole
                            )
                        }
                    )

                    if (uiState is SignupUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as SignupUiState.Error).message,
                            color = Color(0xFFB3261E),
                            style = Typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Row {
                        Text(
                            text = "Already have an account? ",
                            style = Typography.bodyMedium,
                            color = TextSecondaryLightCard
                        )
                        Text(
                            text = "Sign In",
                            style = Typography.bodyMedium,
                            color = Emerald700,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToLogin() }
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