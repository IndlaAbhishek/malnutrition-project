package com.bhanu.malnutritionriskapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryBlue,
    background = BackgroundLight,
    surface = SurfaceWhite,
    error = HighRiskRed
)

@Composable
fun MalnutritionRiskAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
