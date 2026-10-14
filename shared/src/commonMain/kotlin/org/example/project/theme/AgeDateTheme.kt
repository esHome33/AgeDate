package org.example.project.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PersonalLightColors = lightColorScheme(
    background = Color(0xFFF7F4EF),
    onBackground = Color(0xFF1F1B16),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F1B16),
    primary = Color(0xFF3A6EA5),
    onPrimary = Color(0xFFFFFFFF)
)

val PersonalDarkColors = darkColorScheme(
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE6E1DA),
    surface = Color(0xFF24272B),
    onSurface = Color(0xFFE6E1DA),
    primary = Color(0xFF9CC3EA),
    onPrimary = Color(0xFF0B2236)
)

@Composable
fun AgeDateTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) PersonalDarkColors else PersonalLightColors
    MaterialTheme(colorScheme = colors, content = content)
}
