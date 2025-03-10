package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val CatColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFF4DDDE),
                        petCardBackground = Color(0xFFFFDADB),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF8F4A50),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFFFDADB),
                        onPrimaryContainer = Color(0xFF3B0810),
                        secondary = Color(0xFF765658),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFFFDADB),
                        onSecondaryContainer = Color(0xFF2C1517),
                        tertiary = Color(0xFF775930),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFFFDDB3),
                        onTertiaryContainer = Color(0xFF291800),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFFFF8F7),
                        onBackground = Color(0xFF22191A),
                        surface = Color(0xFFFFF8F7),
                        onSurface = Color(0xFF22191A),
                        surfaceVariant = Color(0xFFF4DDDE),
                        onSurfaceVariant = Color(0xFF524344),
                        outline = Color(0xFF857373),
                        outlineVariant = Color(0xFFD7C1C2),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF382E2E),
                        inverseOnSurface = Color(0xFFFFEDED),
                        inversePrimary = Color(0xFFFFB2B7),
                        surfaceDim = Color(0xFFE7D6D6),
                        surfaceBright = Color(0xFFFFF8F7),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFFFF0F0),
                        surfaceContainer = Color(0xFFFCEAEA),
                        surfaceContainerHigh = Color(0xFFF6E4E4),
                        surfaceContainerHighest = Color(0xFFF0DEDE),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF524344),
                        petCardBackground = Color(0xFF9F8C8D),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFFFFB2B7),
                        onPrimary = Color(0xFF561D24),
                        primaryContainer = Color(0xFF723339),
                        onPrimaryContainer = Color(0xFFFFDADB),
                        secondary = Color(0xFFE6BDBE),
                        onSecondary = Color(0xFF44292B),
                        secondaryContainer = Color(0xFF5C3F41),
                        onSecondaryContainer = Color(0xFFFFDADB),
                        tertiary = Color(0xFFE7C08E),
                        onTertiary = Color(0xFF432C06),
                        tertiaryContainer = Color(0xFF5C421A),
                        onTertiaryContainer = Color(0xFFFFDDB3),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF1A1112),
                        onBackground = Color(0xFFF0DEDE),
                        surface = Color(0xFF1A1112),
                        onSurface = Color(0xFFF0DEDE),
                        surfaceVariant = Color(0xFF524344),
                        onSurfaceVariant = Color(0xFFD7C1C2),
                        outline = Color(0xFF9F8C8D),
                        outlineVariant = Color(0xFF524344),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFF0DEDE),
                        inverseOnSurface = Color(0xFF382E2E),
                        inversePrimary = Color(0xFF8F4A50),
                        surfaceDim = Color(0xFF1A1112),
                        surfaceBright = Color(0xFF413737),
                        surfaceContainerLowest = Color(0xFF140C0D),
                        surfaceContainerLow = Color(0xFF22191A),
                        surfaceContainer = Color(0xFF271D1E),
                        surfaceContainerHigh = Color(0xFF312828),
                        surfaceContainerHighest = Color(0xFF3D3233),
                    ),
            ),
    )
