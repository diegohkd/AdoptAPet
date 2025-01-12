package com.mobdao.adoptapet.presentation.screens.filter

import com.mobdao.adoptapet.common.testutils.MainDispatcherRule
import com.mobdao.adoptapet.common.testutils.domain.AddressMockFactory
import com.mobdao.adoptapet.common.testutils.domain.SearchFilterMockFactory
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.domain.usecases.filter.GetSearchFilterUseCase
import com.mobdao.adoptapet.domain.usecases.filter.SaveSearchFilterUseCase
import com.mobdao.adoptapet.presentation.screens.filter.FilterNavAction.FilterApplied
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.ApplyClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeSelected
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FilterViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val address: Address = AddressMockFactory.create(addressLine = "addressLine")
    private val searchFilter =
        SearchFilterMockFactory.create(
            address = address,
            petType = "petType",
        )

    private val getSearchFilterUseCase: GetSearchFilterUseCase =
        mockk {
            every { execute() } returns flowOf(searchFilter)
        }
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase =
        mockk {
            every { execute(SearchFilter(address, "petType")) } returns flowOf(Unit)
        }

    private val tested by lazy {
        FilterViewModel(
            getSearchFilterUseCase = getSearchFilterUseCase,
            saveSearchFilterUseCase = saveSearchFilterUseCase,
        )
    }

    @Test
    fun `given getting search filter throws an exception when initialized then generic error dialog is visible`() {
        // given
        every { getSearchFilterUseCase.execute() } returns flow { throw Exception() }

        // when / then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given getting search filter returns null when initialized then empty initial address is set`() {
        // given
        every { getSearchFilterUseCase.execute() } returns flowOf(null)

        // when / then
        assertEquals(
            tested.uiState.value.initialAddress,
            "",
        )
    }

    @Test
    fun `given getting search filter returns null when initialized then empty pet type is set`() {
        // given
        every { getSearchFilterUseCase.execute() } returns flowOf(null)

        // when / then
        assertEquals(
            tested.uiState.value.petType,
            "",
        )
    }

    @Test
    fun `given getting search filter returns search filter when initialized then initial address is set with address line of filter`() {
        // given / when / then
        assertEquals(
            tested.uiState.value.initialAddress,
            "addressLine",
        )
    }

    @Test
    fun `given getting search filter returns search filter when initialized then pet type is set with pet type of filter`() {
        // given / when / then
        assertEquals(
            tested.uiState.value.petType,
            "petType",
        )
    }

    @Test
    fun `given getting search filter returns search filter with null pet type when initialized then empty pet type is set`() {
        // given
        val searchFilter = SearchFilterMockFactory.create(petType = null)
        every { getSearchFilterUseCase.execute() } returns flowOf(searchFilter)

        // then / then
        assertEquals(
            tested.uiState.value.petType,
            "",
        )
    }

    @Test
    fun `when failed to search address then generic error dialog is visible`() {
        // when
        tested.onUiAction(FailedToGetAddress(throwable = null))

        // then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `when pet type selected then UI is updated with selected pet type`() {
        // given / when
        tested.onUiAction(PetTypeSelected(petType = "type"))

        // then
        assertEquals(
            tested.uiState.value.petType,
            "type",
        )
    }

    @Test
    fun `given search filter selected when apply clicked then search filter is saved`() {
        // given
        val savingSearchFilter = MutableSharedFlow<Unit>()
        every {
            saveSearchFilterUseCase.execute(
                SearchFilter(
                    address = address,
                    petType = "petType",
                ),
            )
        } returns savingSearchFilter

        // when
        tested.onUiAction(ApplyClicked)

        // then
        assertEquals(
            savingSearchFilter.subscriptionCount.value,
            1,
        )
    }

    @Test
    fun `given search filter selected and saving filter throws an exception when apply clicked then generic error is shown`() {
        // given
        every {
            saveSearchFilterUseCase.execute(
                SearchFilter(
                    address = address,
                    petType = "petType",
                ),
            )
        } returns flow { throw Exception() }

        // when
        tested.onUiAction(ApplyClicked)

        // then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given search filter selected and saving filter is successful when apply clicked then filter applied nav action is emitted`() {
        // given / when
        tested.onUiAction(ApplyClicked)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            FilterApplied,
        )
    }

    @Test
    fun `given generic error dialog is visible when it is dismissed then it is hidden`() {
        // given
        every { saveSearchFilterUseCase.execute(any()) } returns flow { throw Exception() }
        tested.onUiAction(ApplyClicked)

        // when
        tested.onUiAction(DismissGenericErrorDialog)

        // then
        assertFalse(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given no address selected when init then apply button is disabled`() {
        // given
        every { getSearchFilterUseCase.execute() } returns flowOf()

        // when / then
        assertFalse(tested.uiState.value.isApplyButtonEnabled)
    }

    @Test
    fun `given address selected when init then apply button is disabled`() {
        // given / when / then
        assertTrue(tested.uiState.value.isApplyButtonEnabled)
    }
}
