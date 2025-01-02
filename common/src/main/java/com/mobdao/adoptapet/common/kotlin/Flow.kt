package com.mobdao.adoptapet.common.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Catch the current flow and Logs the exception.
 * @param action Additional actions for the catch block
 */
fun <T> Flow<T>.catchAndLogException(
    message: String = "",
    action: suspend (Throwable?) -> Unit = {},
): Flow<T> =
    catch {
        Timber.e(it, message)
        action(it)
    }

fun <T> Flow<T>.makeSureTakesAtLeast(
    durationInMillis: Long,
    startTimeInMillis: Long,
): Flow<T> =
    onEach {
        val currentDuration = (System.currentTimeMillis() - startTimeInMillis)
        val delayDuration = (durationInMillis - currentDuration).coerceAtLeast(0L)
        if (delayDuration > 0) {
            delay(delayDuration)
        }
    }
