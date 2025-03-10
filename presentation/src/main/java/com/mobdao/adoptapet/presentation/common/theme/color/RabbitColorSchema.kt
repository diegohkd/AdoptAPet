package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val RabbitColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFDEE3EB),
                        petCardBackground = Color(0xFFCDE5FF),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF2E628C),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFCDE5FF),
                        onPrimaryContainer = Color(0xFF001D32),
                        secondary = Color(0xFF51606F),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFD5E4F6),
                        onSecondaryContainer = Color(0xFF0E1D2A),
                        tertiary = Color(0xFF68587A),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFEEDBFF),
                        onTertiaryContainer = Color(0xFF221533),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFF7F9FF),
                        onBackground = Color(0xFF181C20),
                        surface = Color(0xFFF7F9FF),
                        onSurface = Color(0xFF181C20),
                        surfaceVariant = Color(0xFFDEE3EB),
                        onSurfaceVariant = Color(0xFF42474E),
                        outline = Color(0xFF72777F),
                        outlineVariant = Color(0xFFC2C7CF),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF2D3135),
                        inverseOnSurface = Color(0xFFEEF1F6),
                        inversePrimary = Color(0xFF9ACBFA),
                        surfaceDim = Color(0xFFD8DAE0),
                        surfaceBright = Color(0xFFF7F9FF),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFF1F3F9),
                        surfaceContainer = Color(0xFFECEEF3),
                        surfaceContainerHigh = Color(0xFFE6E8EE),
                        surfaceContainerHighest = Color(0xFFE0E2E8),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF42474E),
                        petCardBackground = Color(0xFF8C9198),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFF9ACBFA),
                        onPrimary = Color(0xFF003352),
                        primaryContainer = Color(0xFF0C4A72),
                        onPrimaryContainer = Color(0xFFCDE5FF),
                        secondary = Color(0xFFB9C8DA),
                        onSecondary = Color(0xFF233240),
                        secondaryContainer = Color(0xFF3A4857),
                        onSecondaryContainer = Color(0xFFD5E4F6),
                        tertiary = Color(0xFFD3BFE6),
                        onTertiary = Color(0xFF382A49),
                        tertiaryContainer = Color(0xFF4F4061),
                        onTertiaryContainer = Color(0xFFEEDBFF),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF101418),
                        onBackground = Color(0xFFE0E2E8),
                        surface = Color(0xFF101418),
                        onSurface = Color(0xFFE0E2E8),
                        surfaceVariant = Color(0xFF42474E),
                        onSurfaceVariant = Color(0xFFC2C7CF),
                        outline = Color(0xFF8C9198),
                        outlineVariant = Color(0xFF42474E),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFE0E2E8),
                        inverseOnSurface = Color(0xFF2D3135),
                        inversePrimary = Color(0xFF2E628C),
                        surfaceDim = Color(0xFF101418),
                        surfaceBright = Color(0xFF36393E),
                        surfaceContainerLowest = Color(0xFF0B0F12),
                        surfaceContainerLow = Color(0xFF181C20),
                        surfaceContainer = Color(0xFF1C2024),
                        surfaceContainerHigh = Color(0xFF272A2F),
                        surfaceContainerHighest = Color(0xFF323539),
                    ),
            ),
    )
