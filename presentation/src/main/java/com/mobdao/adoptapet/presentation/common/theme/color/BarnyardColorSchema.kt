package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val BarnyardColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFE9E2D0),
                        petCardBackground = Color(0xFFECE3BC),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF6B5F10),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFF5E389),
                        onPrimaryContainer = Color(0xFF211C00),
                        secondary = Color(0xFF655F41),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFECE3BC),
                        onSecondaryContainer = Color(0xFF201C05),
                        tertiary = Color(0xFF426650),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFC3ECD0),
                        onTertiaryContainer = Color(0xFF002111),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFFFF9EB),
                        onBackground = Color(0xFF1D1C13),
                        surface = Color(0xFFFFF9EB),
                        onSurface = Color(0xFF1D1C13),
                        surfaceVariant = Color(0xFFE9E2D0),
                        onSurfaceVariant = Color(0xFF4A4739),
                        outline = Color(0xFF7B7768),
                        outlineVariant = Color(0xFFCCC6B5),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF333027),
                        inverseOnSurface = Color(0xFFF6F0E2),
                        inversePrimary = Color(0xFFD8C770),
                        surfaceDim = Color(0xFFDFDACC),
                        surfaceBright = Color(0xFFFFF9EB),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFF9F3E5),
                        surfaceContainer = Color(0xFFF3EDE0),
                        surfaceContainerHigh = Color(0xFFEEE8DA),
                        surfaceContainerHighest = Color(0xFFE8E2D4),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF4A4739),
                        petCardBackground = Color(0xFF969180),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFFD8C770),
                        onPrimary = Color(0xFF383000),
                        primaryContainer = Color(0xFF514700),
                        onPrimaryContainer = Color(0xFFF5E389),
                        secondary = Color(0xFFD0C7A2),
                        onSecondary = Color(0xFF353116),
                        secondaryContainer = Color(0xFF4D472B),
                        onSecondaryContainer = Color(0xFFECE3BC),
                        tertiary = Color(0xFFA8D0B5),
                        onTertiary = Color(0xFF123724),
                        tertiaryContainer = Color(0xFF2A4E3A),
                        onTertiaryContainer = Color(0xFFC3ECD0),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF15130C),
                        onBackground = Color(0xFFE8E2D4),
                        surface = Color(0xFF15130C),
                        onSurface = Color(0xFFE8E2D4),
                        surfaceVariant = Color(0xFF4A4739),
                        onSurfaceVariant = Color(0xFFCCC6B5),
                        outline = Color(0xFF969180),
                        outlineVariant = Color(0xFF4A4739),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFE8E2D4),
                        inverseOnSurface = Color(0xFF333027),
                        inversePrimary = Color(0xFF6B5F10),
                        surfaceDim = Color(0xFF15130C),
                        surfaceBright = Color(0xFF3C3930),
                        surfaceContainerLowest = Color(0xFF100E07),
                        surfaceContainerLow = Color(0xFF1D1C13),
                        surfaceContainer = Color(0xFF222017),
                        surfaceContainerHigh = Color(0xFF2C2A21),
                        surfaceContainerHighest = Color(0xFF37352B),
                    ),
            ),
    )
