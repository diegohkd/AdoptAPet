package com.mobdao.adoptapet.presentation.common

/**
 * Used as a wrapper for data that is exposed via a Flow that represents an event.
 */
open class Event<out T>(
    private val content: T,
) {
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
