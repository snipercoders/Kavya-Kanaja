package com.example.kavyakanaja.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KavyaColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700),
    secondary = Color(0xFFDEB887),
    tertiary = Color(0xFF8B4513),
    background = Color(0xFF2C0A0A),
    surface = Color(0xFF5C1A1A),
    onPrimary = Color(0xFF2C0A0A),
    onSecondary = Color(0xFF2C0A0A),
    onBackground = Color(0xFFFFFAF0),
    onSurface = Color(0xFFFFFAF0),
)

@Composable
fun KavyaKanajaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KavyaColorScheme,
        content = content
    )
}