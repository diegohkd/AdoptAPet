package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DogColorSchema =
    ColorSchema(
        light =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFFDBE5DF),
                        petCardBackground = Color(0xFFCDE9DE),
                    ),
                materialColorScheme =
                    lightColorScheme(
                        primary = Color(0xFF0F6B57),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFA2F2D9),
                        onPrimaryContainer = Color(0xFF002019),
                        secondary = Color(0xFF4B635B),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFCDE9DE),
                        onSecondaryContainer = Color(0xFF072019),
                        tertiary = Color(0xFF416277),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFC5E7FF),
                        onTertiaryContainer = Color(0xFF001E2D),
                        error = Color(0xFFBA1A1A),
                        onError = Color(0xFFFFFFFF),
                        errorContainer = Color(0xFFFFDAD6),
                        onErrorContainer = Color(0xFF410002),
                        background = Color(0xFFF5FBF6),
                        onBackground = Color(0xFF171D1B),
                        surface = Color(0xFFF5FBF6),
                        onSurface = Color(0xFF171D1B),
                        surfaceVariant = Color(0xFFDBE5DF),
                        onSurfaceVariant = Color(0xFF3F4945),
                        outline = Color(0xFF6F7975),
                        outlineVariant = Color(0xFFBFC9C4),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFF2B322F),
                        inverseOnSurface = Color(0xFFECF2EE),
                        inversePrimary = Color(0xFF86D6BE),
                        surfaceDim = Color(0xFFD5DBD7),
                        surfaceBright = Color(0xFFF5FBF6),
                        surfaceContainerLowest = Color(0xFFFFFFFF),
                        surfaceContainerLow = Color(0xFFEFF5F1),
                        surfaceContainer = Color(0xFFE9EFEB),
                        surfaceContainerHigh = Color(0xFFE3EAE5),
                        surfaceContainerHighest = Color(0xFFDEE4E0),
                    ),
            ),
        dark =
            AdoptAPetColorScheme(
                petColorScheme =
                    PetColorScheme(
                        background = Color(0xFF3F4945),
                        petCardBackground = Color(0xFF89938E),
                    ),
                materialColorScheme =
                    darkColorScheme(
                        primary = Color(0xFF86D6BE),
                        onPrimary = Color(0xFF00382C),
                        primaryContainer = Color(0xFF005141),
                        onPrimaryContainer = Color(0xFFA2F2D9),
                        secondary = Color(0xFFB2CCC2),
                        onSecondary = Color(0xFF1D352E),
                        secondaryContainer = Color(0xFF344C44),
                        onSecondaryContainer = Color(0xFFCDE9DE),
                        tertiary = Color(0xFFA9CBE2),
                        onTertiary = Color(0xFF0E3446),
                        tertiaryContainer = Color(0xFF294B5E),
                        onTertiaryContainer = Color(0xFFC5E7FF),
                        error = Color(0xFFFFB4AB),
                        onError = Color(0xFF690005),
                        errorContainer = Color(0xFF93000A),
                        onErrorContainer = Color(0xFFFFDAD6),
                        background = Color(0xFF0F1512),
                        onBackground = Color(0xFFDEE4E0),
                        surface = Color(0xFF0F1512),
                        onSurface = Color(0xFFDEE4E0),
                        surfaceVariant = Color(0xFF3F4945),
                        onSurfaceVariant = Color(0xFFBFC9C4),
                        outline = Color(0xFF89938E),
                        outlineVariant = Color(0xFF3F4945),
                        scrim = Color(0xFF000000),
                        inverseSurface = Color(0xFFDEE4E0),
                        inverseOnSurface = Color(0xFF2B322F),
                        inversePrimary = Color(0xFF0F6B57),
                        surfaceDim = Color(0xFF0F1512),
                        surfaceBright = Color(0xFF343B38),
                        surfaceContainerLowest = Color(0xFF090F0D),
                        surfaceContainerLow = Color(0xFF171D1B),
                        surfaceContainer = Color(0xFF1B211F),
                        surfaceContainerHigh = Color(0xFF252B29),
                        surfaceContainerHighest = Color(0xFF303633),
                    ),
            ),
    )
