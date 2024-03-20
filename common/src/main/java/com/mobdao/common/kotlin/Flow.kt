package com.mobdao.common.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

/**
 * Catch the current flow and Logs the exception.
 * @param action Additional actions for the catch block
 */
fun <T> Flow<T>.catchAndLogException(
    message: String = "",
    action: suspend (Throwable?) -> Unit = {}
): Flow<T> = catch {
    Timber.e(it, message)
    action(it)
}