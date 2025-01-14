package com.mobdao.adoptapet.presentation.common.utils.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T : Any> NavGraphBuilder.composableHorizontalAnimated(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = {
            fadeIn(
                animationSpec =
                    tween(
                        durationMillis = 300,
                        easing = LinearEasing,
                    ),
            ) +
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = Start,
                )
        },
        exitTransition = {
            fadeOut(
                animationSpec =
                    tween(
                        durationMillis = 300,
                        easing = LinearEasing,
                    ),
            ) +
                slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = End,
                )
        },
        content = content,
    )
}
