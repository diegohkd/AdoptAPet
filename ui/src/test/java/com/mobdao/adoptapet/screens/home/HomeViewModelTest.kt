package com.mobdao.adoptapet.screens.home

import com.mobdao.adoptapet.screens.home.petspaging.PetsPager
import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.common.testutils.mockfactories.domain.SearchFilterMockFactory
import com.mobdao.domain.usecases.filter.ObserveSearchFilterUseCase
import com.mobdao.domain.models.SearchFilter
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val searchFilter: SearchFilter =
        SearchFilterMockFactory.create(
            AddressMockFactory.create(addressLine = "addressLine")
        )

    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase = mockk {
        every { execute() } returns flowOf(searchFilter)
    }
    private val updateCachedPetsFilterWithCurrentLocationUseCase: UpdateCachedPetsFilterWithCurrentLocationUseCase =
        mockk()
    private val petsPager: PetsPager = mockk {
        every { items } returns flowOf()
        justRun { setFilterAndRefresh(searchFilter) }
    }

    private val tested by lazy {
        HomeViewModel(
            observeSearchFilterUseCase = observeSearchFilterUseCase,
            updateCachedPetsFilterWithCurrentLocationUseCase = updateCachedPetsFilterWithCurrentLocationUseCase,
            petsPager = petsPager,
        )
    }

    @Test
    fun `when initialized then it observes location permission state`() {
        // when / then
        assertTrue(tested.uiState.value.observeLocationPermissionState)
    }

    @Test
    fun `given observing search filter throws an exception when initialized then generic error dialog is shown`() {
        // given
        every { observeSearchFilterUseCase.execute() } returns flow { throw RuntimeException() }

        // when / then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given search filter is null when initialized then address is not set`() {
        // given
        every { observeSearchFilterUseCase.execute() } returns flowOf(null)

        // when / then
        assertEquals(
            tested.uiState.value.address,
            ""
        )
    }

    @Test
    fun `given search filter is not null when initialized then address UI state is set`() {
        // when / then
        assertEquals(
            tested.uiState.value.address,
            "addressLine"
        )
    }

    @Test
    fun `given search filter is null when initialized then pets pager does not set filter and refresh`() {
        // given
        every { observeSearchFilterUseCase.execute() } returns flowOf(null)

        // when
        tested

        // then
        verify(exactly = 0) { petsPager.setFilterAndRefresh(any()) }
    }

    @Test
    fun `given search filter is not null when initialized then pets pager sets filter and refreshes`() {
        // given / when
        tested

        // then
        verify { petsPager.setFilterAndRefresh(searchFilter) }
    }

    @Test
    fun `given location permission granted when permission state updated then stops observing location permission state`() {
        // given / when
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // then
        assertFalse(tested.uiState.value.observeLocationPermissionState)
    }

    @Test
    fun `given location permission granted when permission state updated then updates cached Pets filter with current location`() =
        runTest {
            // given
            val mutableSharedFlow = MutableSharedFlow<Unit>()
            every { updateCachedPetsFilterWithCurrentLocationUseCase.execute() } returns mutableSharedFlow

            // when
            tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

            // then
            assertEquals(mutableSharedFlow.subscriptionCount.value, 1)
        }

    // TODO test locationPlaceholderIsVisible setting to false
    // TODO test isUpdatingFilterWithCurrentLocation setting to true AND false

    @Test
    fun `given location permission granted and updating cached Pets filter throws exception when permission state updated then generic error dialog is visible`() =
        runTest {
            // given
            every { updateCachedPetsFilterWithCurrentLocationUseCase.execute() } returns flow { throw Exception() }

            // when
            tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

            // then
            assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
        }
}