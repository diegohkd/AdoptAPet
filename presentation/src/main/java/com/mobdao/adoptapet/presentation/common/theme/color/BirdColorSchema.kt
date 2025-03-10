package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val BirdColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFE9E0EB),
                        petCardBackground = Color(0xFFECDDF6),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF6D538B),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFEFDBFF),
                        onPrimaryContainer = Color(0xFF270D43),
                        secondary = Color(0xFF655A6F),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFECDDF6),
                        onSecondaryContainer = Color(0xFF20182A),
                        tertiary = Color(0xFF805159),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFFFD9DE),
                        onTertiaryContainer = Color(0xFF321018),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFFFF7FF),
                        onBackground = Color(0xFF1D1A20),
                        surface = Color(0xFFFFF7FF),
                        onSurface = Color(0xFF1D1A20),
                        surfaceVariant = Color(0xFFE9E0EB),
                        onSurfaceVariant = Color(0xFF4A454E),
                        outline = Color(0xFF7B757F),
                        outlineVariant = Color(0xFFCCC4CF),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF332F35),
                        inverseOnSurface = Color(0xFFF6EEF6),
                        inversePrimary = Color(0xFFD9BAFA),
                        surfaceDim = Color(0xFFDFD8E0),
                        surfaceBright = Color(0xFFFFF7FF),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFF9F1F9),
                        surfaceContainer = Color(0xFFF3EBF3),
                        surfaceContainerHigh = Color(0xFFEDE6EE),
                        surfaceContainerHighest = Color(0xFFE8E0E8),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF4A454E),
                        petCardBackground = Color(0xFF958E98),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFFD9BAFA),
                        onPrimary = Color(0xFF3D2459),
                        primaryContainer = Color(0xFF543B72),
                        onPrimaryContainer = Color(0xFFEFDBFF),
                        secondary = Color(0xFFCFC1DA),
                        onSecondary = Color(0xFF362D40),
                        secondaryContainer = Color(0xFF4D4357),
                        onSecondaryContainer = Color(0xFFECDDF6),
                        tertiary = Color(0xFFF2B7BF),
                        onTertiary = Color(0xFF4B252C),
                        tertiaryContainer = Color(0xFF653B42),
                        onTertiaryContainer = Color(0xFFFFD9DE),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF151218),
                        onBackground = Color(0xFFE8E0E8),
                        surface = Color(0xFF151218),
                        onSurface = Color(0xFFE8E0E8),
                        surfaceVariant = Color(0xFF4A454E),
                        onSurfaceVariant = Color(0xFFCCC4CF),
                        outline = Color(0xFF958E98),
                        outlineVariant = Color(0xFF4A454E),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFE8E0E8),
                        inverseOnSurface = Color(0xFF332F35),
                        inversePrimary = Color(0xFF6D538B),
                        surfaceDim = Color(0xFF151218),
                        surfaceBright = Color(0xFF3C383E),
                        surfaceContainerLowest = Color(0xFF100D12),
                        surfaceContainerLow = Color(0xFF1D1A20),
                        surfaceContainer = Color(0xFF221E24),
                        surfaceContainerHigh = Color(0xFF2C292F),
                        surfaceContainerHighest = Color(0xFF373339),
                    ),
            ),
    )
