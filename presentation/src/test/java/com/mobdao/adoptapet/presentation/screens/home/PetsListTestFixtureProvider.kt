package com.mobdao.adoptapet.presentation.screens.home

import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import io.mockk.mockk

object PetsListTestFixtureProvider {
    data class GivenParams(
        val refreshLoadState: LoadState,
        val appendLoadState: LoadState,
        val itemsCount: Int,
    )

    data class ExpectedUiState(
        val emptyListPlaceholderIsVisible: Boolean,
        val genericErrorDialogIsVisible: Boolean,
        val progressIndicatorIsVisible: Boolean,
        val nextPageProgressIndicatorIsVisible: Boolean,
    )

    private val error = Error(Exception())

    @Suppress("UNUSED", "LongMethod")
    @JvmStatic
    fun provide(): Array<Array<Any>> =
        arrayOf(
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = true,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = Loading,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = Loading,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = error,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = mockk<NotLoading>(),
                    appendLoadState = error,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = Loading,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = Loading,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = false,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = error,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = Loading,
                    appendLoadState = error,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = true,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = NotLoading(endOfPaginationReached = false),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = NotLoading(endOfPaginationReached = true),
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = Loading,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = Loading,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = true,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = error,
                    itemsCount = 0,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
            arrayOf(
                GivenParams(
                    refreshLoadState = error,
                    appendLoadState = error,
                    itemsCount = 1,
                ),
                ExpectedUiState(
                    emptyListPlaceholderIsVisible = false,
                    genericErrorDialogIsVisible = true,
                    progressIndicatorIsVisible = false,
                    nextPageProgressIndicatorIsVisible = false,
                ),
            ),
        )
}
