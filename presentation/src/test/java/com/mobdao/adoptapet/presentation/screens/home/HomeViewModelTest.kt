package com.mobdao.adoptapet.presentation.screens.home

import androidx.paging.LoadState.Error
import com.mobdao.adoptapet.common.testutils.MainDispatcherRule
import com.mobdao.adoptapet.common.testutils.domain.AddressMockFactory
import com.mobdao.adoptapet.common.testutils.domain.SearchFilterMockFactory
import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.domain.usecases.filter.CreateAndCachePetsFilterWithCachedLocationUseCase
import com.mobdao.adoptapet.domain.usecases.filter.ObserveSearchFilterUseCase
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.FilterClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.PetClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import com.mobdao.adoptapet.presentation.screens.home.petspaging.PetsPager
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class HomeViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val searchFilter: SearchFilter =
        SearchFilterMockFactory.create(
            AddressMockFactory.create(addressLine = "addressLine"),
        )

    private val createAndCachePetsFilterWithCachedLocationUseCase: CreateAndCachePetsFilterWithCachedLocationUseCase =
        mockk {
            every { execute() } returns flowOf(Unit)
        }
    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase =
        mockk {
            every { execute() } returns flowOf(searchFilter)
        }
    private val petsPager: PetsPager =
        mockk {
            every { items } returns flowOf()
            justRun { setFilterAndRefresh(searchFilter) }
        }

    private val tested by lazy {
        HomeViewModel(
            createAndCachePetsFilterWithCachedLocationUseCase = createAndCachePetsFilterWithCachedLocationUseCase,
            observeSearchFilterUseCase = observeSearchFilterUseCase,
            petsPager = petsPager,
        )
    }

    @Test
    fun `when initialized then pets filter is created with cached location`() {
        // given
        val creatingAndCachingPetsFilterWithCachedLocation = MutableSharedFlow<Unit>()
        every {
            createAndCachePetsFilterWithCachedLocationUseCase.execute()
        } returns creatingAndCachingPetsFilterWithCachedLocation

        // when
        tested

        // then
        assertEquals(
            creatingAndCachingPetsFilterWithCachedLocation.subscriptionCount.value,
            1,
        )
    }

    @Test
    fun `given creating and caching pets filter with cached location throws exception when initialized then generic error dialog is shown`() {
        // given
        every { createAndCachePetsFilterWithCachedLocationUseCase.execute() } returns flow { throw Exception() }

        // when
        tested

        // then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            true,
        )
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
            "",
        )
    }

    @Test
    fun `given search filter is not null when initialized then address UI state is set`() {
        // when / then
        assertEquals(
            tested.uiState.value.address,
            "addressLine",
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
    @Parameters(source = PetsListTestFixtureProvider::class)
    @TestCaseName("given parameters {0} when pets list load state update then expect UI state updated to {1}")
    fun `given parameters when pets list load state update then correct UI state is updated`(
        givenParams: PetsListTestFixtureProvider.GivenParams,
        expectedUiState: PetsListTestFixtureProvider.ExpectedUiState,
    ) {
        // given / when
        tested.onPetsListLoadStateUpdate(
            refreshLoadState = givenParams.refreshLoadState,
            appendLoadState = givenParams.appendLoadState,
            itemsCount = givenParams.itemsCount,
        )

        // then
        assertEquals(
            tested.uiState.value.emptyListPlaceholderIsVisible,
            expectedUiState.emptyListPlaceholderIsVisible,
        )
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            expectedUiState.genericErrorDialogIsVisible,
        )
        assertEquals(
            tested.uiState.value.progressIndicatorIsVisible,
            expectedUiState.progressIndicatorIsVisible,
        )
        assertEquals(
            tested.uiState.value.nextPageProgressIndicatorIsVisible,
            expectedUiState.nextPageProgressIndicatorIsVisible,
        )
    }

    @Test
    fun `when Pet is clicked then Pet clicked nav action is emitted`() {
        // given
        val animalType: AnimalType = mockk<AnimalType>()
        val pet: PetState =
            mockk {
                every { id } returns "pet-id"
                every { type } returns animalType
            }
        // when
        tested.onUiAction(HomeUiAction.PetClicked(pet = pet))

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            PetClicked(petId = "pet-id", type = animalType),
        )
    }

    @Test
    fun `when filter button is clicked then filter clicked nav action is emitted`() {
        // when
        tested.onUiAction(HomeUiAction.FilterClicked)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            FilterClicked,
        )
    }

    @Test
    fun `given generic error dialog is visible when dismiss the dialog then the dialog is hidden`() {
        // given
        tested.onPetsListLoadStateUpdate(
            refreshLoadState = Error(Exception()),
            appendLoadState = Error(Exception()),
            itemsCount = 0,
        )

        // when
        tested.onUiAction(HomeUiAction.DismissGenericErrorDialog)

        // then
        assertFalse(tested.uiState.value.genericErrorDialogIsVisible)
    }
}
