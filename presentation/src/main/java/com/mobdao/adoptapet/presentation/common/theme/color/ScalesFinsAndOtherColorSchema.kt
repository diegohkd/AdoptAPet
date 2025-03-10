package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val ScalesFindsAndOtherColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFDBE4E7),
                        petCardBackground = Color(0xFFABEDFF),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF00687A),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFABEDFF),
                        onPrimaryContainer = Color(0xFF001F26),
                        secondary = Color(0xFF4B6269),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFCEE7EE),
                        onSecondaryContainer = Color(0xFF061F24),
                        tertiary = Color(0xFF565D7E),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFDDE1FF),
                        onTertiaryContainer = Color(0xFF131A37),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFF5FAFC),
                        onBackground = Color(0xFF171C1E),
                        surface = Color(0xFFF5FAFC),
                        onSurface = Color(0xFF171C1E),
                        surfaceVariant = Color(0xFFDBE4E7),
                        onSurfaceVariant = Color(0xFF3F484B),
                        outline = Color(0xFF70797B),
                        outlineVariant = Color(0xFFBFC8CB),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF2C3133),
                        inverseOnSurface = Color(0xFFECF2F4),
                        inversePrimary = Color(0xFF84D2E6),
                        surfaceDim = Color(0xFFD5DBDD),
                        surfaceBright = Color(0xFFF5FAFC),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFEFF4F6),
                        surfaceContainer = Color(0xFFE9EFF1),
                        surfaceContainerHigh = Color(0xFFE4E9EB),
                        surfaceContainerHighest = Color(0xFFDEE3E5),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF3F484B),
                        petCardBackground = Color(0xFF899295),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFF84D2E6),
                        onPrimary = Color(0xFF003640),
                        primaryContainer = Color(0xFF004E5C),
                        onPrimaryContainer = Color(0xFFABEDFF),
                        secondary = Color(0xFFB2CBD2),
                        onSecondary = Color(0xFF1D343A),
                        secondaryContainer = Color(0xFF334A51),
                        onSecondaryContainer = Color(0xFFCEE7EE),
                        tertiary = Color(0xFFBEC4EB),
                        onTertiary = Color(0xFF282F4D),
                        tertiaryContainer = Color(0xFF3F4565),
                        onTertiaryContainer = Color(0xFFDDE1FF),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF0F1416),
                        onBackground = Color(0xFFDEE3E5),
                        surface = Color(0xFF0F1416),
                        onSurface = Color(0xFFDEE3E5),
                        surfaceVariant = Color(0xFF3F484B),
                        onSurfaceVariant = Color(0xFFBFC8CB),
                        outline = Color(0xFF899295),
                        outlineVariant = Color(0xFF3F484B),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFDEE3E5),
                        inverseOnSurface = Color(0xFF2C3133),
                        inversePrimary = Color(0xFF00687A),
                        surfaceDim = Color(0xFF0F1416),
                        surfaceBright = Color(0xFF343A3C),
                        surfaceContainerLowest = Color(0xFF090F11),
                        surfaceContainerLow = Color(0xFF171C1E),
                        surfaceContainer = Color(0xFF1B2022),
                        surfaceContainerHigh = Color(0xFF252B2D),
                        surfaceContainerHighest = Color(0xFF303638),
                    ),
            ),
    )
