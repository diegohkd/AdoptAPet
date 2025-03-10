package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val HorseColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFF3DFD1),
                        petCardBackground = Color(0xFFFFDCC2),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF88511D),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFFFDCC2),
                        onPrimaryContainer = Color(0xFF2E1500),
                        secondary = Color(0xFF745944),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFFFDCC2),
                        onSecondaryContainer = Color(0xFF2A1707),
                        tertiary = Color(0xFF5B6237),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFE0E7B1),
                        onTertiaryContainer = Color(0xFF191E00),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFFFF8F5),
                        onBackground = Color(0xFF221A14),
                        surface = Color(0xFFFFF8F5),
                        onSurface = Color(0xFF221A14),
                        surfaceVariant = Color(0xFFF3DFD1),
                        onSurfaceVariant = Color(0xFF51443B),
                        outline = Color(0xFF847469),
                        outlineVariant = Color(0xFFD6C3B6),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF382F28),
                        inverseOnSurface = Color(0xFFFEEEE4),
                        inversePrimary = Color(0xFFFFB77B),
                        surfaceDim = Color(0xFFE7D7CD),
                        surfaceBright = Color(0xFFFFF8F5),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFFFF1E8),
                        surfaceContainer = Color(0xFFFBEBE1),
                        surfaceContainerHigh = Color(0xFFF5E5DB),
                        surfaceContainerHighest = Color(0xFFEFE0D6),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF51443B),
                        petCardBackground = Color(0xFF9E8E82),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFFFFB77B),
                        onPrimary = Color(0xFF4C2700),
                        primaryContainer = Color(0xFF6B3A05),
                        onPrimaryContainer = Color(0xFFFFDCC2),
                        secondary = Color(0xFFE3C0A5),
                        onSecondary = Color(0xFF412C19),
                        secondaryContainer = Color(0xFF5A422E),
                        onSecondaryContainer = Color(0xFFFFDCC2),
                        tertiary = Color(0xFFC4CB97),
                        onTertiary = Color(0xFF2D330D),
                        tertiaryContainer = Color(0xFF444A22),
                        onTertiaryContainer = Color(0xFFE0E7B1),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF19120C),
                        onBackground = Color(0xFFEFE0D6),
                        surface = Color(0xFF19120C),
                        onSurface = Color(0xFFEFE0D6),
                        surfaceVariant = Color(0xFF51443B),
                        onSurfaceVariant = Color(0xFFD6C3B6),
                        outline = Color(0xFF9E8E82),
                        outlineVariant = Color(0xFF51443B),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFEFE0D6),
                        inverseOnSurface = Color(0xFF382F28),
                        inversePrimary = Color(0xFF88511D),
                        surfaceDim = Color(0xFF19120C),
                        surfaceBright = Color(0xFF413731),
                        surfaceContainerLowest = Color(0xFF140D08),
                        surfaceContainerLow = Color(0xFF221A14),
                        surfaceContainer = Color(0xFF261E18),
                        surfaceContainerHigh = Color(0xFF312822),
                        surfaceContainerHighest = Color(0xFF3C332C),
                    ),
            ),
    )
