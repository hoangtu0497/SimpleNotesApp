package com.torilab.assignment.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun MyNoteAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = CobaltBlueLightPrimary,
            secondary = CobaltBlueLightSecondary,
            tertiary = LightTertiary,
            background = LightBackground,
            surface = LightTertiary,
            onPrimary = LightTertiary,
            onSurface = LightOnSurface,
        ),
        typography = Typography,
        content = content,
    )
}
