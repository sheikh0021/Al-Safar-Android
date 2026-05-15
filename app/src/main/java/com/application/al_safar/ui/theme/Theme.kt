package com.application.al_safar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AlSafarScheme = darkColorScheme(
    primary = Emerald700,
    onPrimary = TextPrimaryDarkBg,
    secondary = Gold500,
    onSecondary = TextPrimaryLightCard,
    background = Emerald900,
    onBackground = TextPrimaryDarkBg,
    surface = Emerald700,
    onSurface = TextPrimaryDarkBg,
    surfaceVariant = CardLight,
    onSurfaceVariant = TextPrimaryLightCard
)

@Composable
fun AlSafarTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.statusBarColor = Emerald900.toArgb()
            window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = AlSafarScheme,
        typography = Typography,
        content = content
    )
}