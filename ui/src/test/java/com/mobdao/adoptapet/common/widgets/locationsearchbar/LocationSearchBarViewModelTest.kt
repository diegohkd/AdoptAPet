package com.mobdao.adoptapet.common.widgets.locationsearchbar

import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.domain.models.Address
import com.mobdao.domain.usecases.location.GetAutocompleteLocationOptionsUseCase
import com.mobdao.domain.usecases.location.GetCurrentLocationUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocationSearchBarViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val currentAddress = AddressMockFactory.create("current-address-line")
    private val addressAutocompleteResult1 = AddressMockFactory.create(addressLine = "addressLine1")
    private val addressAutocompleteResult2 = AddressMockFactory.create(addressLine = "addressLine2")
    private val autocompleteResult = listOf(addressAutocompleteResult1, addressAutocompleteResult2)

    private val getAutocompleteLocationOptionsUseCase: GetAutocompleteLocationOptionsUseCase =
        mockk {
            every { execute("locationQuery") } returns flowOf(autocompleteResult)
        }
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase = mockk {
        every { execute() } returns flowOf(currentAddress)
    }

    private val tested by lazy {
        LocationSearchBarViewModel(
            getAutocompleteLocationOptionsUseCase = getAutocompleteLocationOptionsUseCase,
            getCurrentLocationUseCase = getCurrentLocationUseCase,
        )
    }

    @Test
    fun `when initialized with initial address then search query is set to the address`() {
        // given / when
        tested.init(address = "address")

        // then
        assertEquals(
            tested.locationSearchQuery,
            "address"
        )
    }

    @Test
    fun `given location permission not granted when current location clicked then location permission is requested`() {
        // given
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = false)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertNotNull(tested.requestLocationPermission.value)
    }

    @Test
    fun `given location permission granted when current location clicked then current location is fetched`() {
        // given
        val gettingCurrentLocation = MutableSharedFlow<Address>()
        every { getCurrentLocationUseCase.execute() } returns gettingCurrentLocation
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertEquals(
            gettingCurrentLocation.subscriptionCount.value,
            1
        )
    }

    @Test
    fun `when current location clicked and current location is fetched then progress indicator is shown`() {
        // given
        every { getCurrentLocationUseCase.execute() } returns flowOf()
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertTrue(tested.uiState.value.progressIndicatorIsVisible)
    }

    @Test
    fun `when current location clicked and getting current location fails then progress indicator is hidden`() {
        // given
        every { getCurrentLocationUseCase.execute() } returns flow { throw Exception() }
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertFalse(tested.uiState.value.progressIndicatorIsVisible)
    }

    @Test
    fun `when current location clicked and getting current location fails then error encountered event is emitted`() {
        // given
        every { getCurrentLocationUseCase.execute() } returns flow { throw Exception() }
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertNotNull(tested.errorEncountered.value)
    }

    @Test
    fun `when current location clicked and successfully gets current location then progress indicator is hidden`() {
        // given
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertFalse(tested.uiState.value.progressIndicatorIsVisible)
    }

    @Test
    fun `given search mode is active when current location clicked and successfully gets current location then search mode is deactivated`() {
        // given
        tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

        // when
        tested.onCurrentLocationClicked()

        // then
        assertFalse(tested.uiState.value.searchModeIsActive)
    }

    @Test
    fun `given search query returned location results when current location clicked and successfully gets current location then autocomplete results are cleared`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)
            tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

            // when
            tested.onCurrentLocationClicked()

            // then
            assertEquals(
                tested.uiState.value.locationAutocompleteAddresses,
                emptyList<String>()
            )
        }

    @Test
    fun `given search query returned location results when current location clicked and successfully gets current location then search query UI state is updated to current address line`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)
            tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

            // when
            tested.onCurrentLocationClicked()

            // then
            assertEquals(
                tested.locationSearchQuery,
                "current-address-line"
            )
        }

    @Test
    fun `given search query returned location results when current location clicked and successfully gets current location then address selected event is emitted`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)
            tested.onLocationPermissionStateUpdated(areAllLocationPermissionsGranted = true)

            // when
            tested.onCurrentLocationClicked()

            // then
            assertEquals(
                tested.addressSelected.value!!.peekContent()!!.address,
                currentAddress
            )
        }

    @Test
    fun `given new search query when location query changed then search query UI state is updated with new query`() {
        // given / when
        tested.onLocationSearchQueryChanged("locationQuery")

        // then
        assertEquals(
            tested.locationSearchQuery,
            "locationQuery"
        )
    }

    @Test
    fun `given location query changed when less than 1000ms has passed then autocomplete search is not requested`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(900)

            // then
            verify(exactly = 0) { getAutocompleteLocationOptionsUseCase.execute(any()) }
        }

    @Test
    fun `given location query changed when more than 1000ms has passed then autocomplete search is requested`() =
        runTest {
            // given
            val autocompletionSearching = MutableSharedFlow<List<Address>>()
            every { getAutocompleteLocationOptionsUseCase.execute(any()) } returns autocompletionSearching
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(1100)

            // then
            assertEquals(
                autocompletionSearching.subscriptionCount.value,
                1
            )
        }

    @Test
    fun `given location query changed when more than 1000ms has passed then progress indicator is visible`() =
        runTest {
            // given
            val autocompletionSearching = MutableSharedFlow<List<Address>>()
            every { getAutocompleteLocationOptionsUseCase.execute(any()) } returns autocompletionSearching
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(1100)

            // then
            assertTrue(tested.uiState.value.progressIndicatorIsVisible)
        }

    @Test
    fun `given location query changed and searching autocomplete options throws exception when more than 1000ms has passed then progress indicator is hidden`() =
        runTest {
            // given
            every { getAutocompleteLocationOptionsUseCase.execute(any()) } returns flow { throw Exception() }
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(1100)

            // then
            assertFalse(tested.uiState.value.progressIndicatorIsVisible)
        }

    @Test
    fun `given location query changed and searching autocomplete options throws exception when more than 1000ms has passed then error event is emitted`() =
        runTest {
            // given
            val exception = Exception()
            every { getAutocompleteLocationOptionsUseCase.execute(any()) } returns flow { throw exception }
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(1100)

            // then
            assertEquals(
                tested.errorEncountered.value!!.peekContent(),
                exception
            )
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful results when more than 1000ms has passed then autocomplete options are displayed on the UI`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")

            // then
            advanceTimeBy(1100)

            // then
            assertEquals(
                tested.uiState.value.locationAutocompleteAddresses,
                listOf("addressLine1", "addressLine2")
            )
        }

    @Test
    fun `when search mode active status updated to true then search mode is active`() {
        // given / when
        tested.onSearchModeActiveChange(isActive = true)

        // then
        assertTrue(tested.uiState.value.searchModeIsActive)
    }

    @Test
    fun `given search mode was active when search mode active status updated to false then search mode is deactivated`() {
        // given
        tested.onSearchModeActiveChange(isActive = true)

        // when
        tested.onSearchModeActiveChange(isActive = false)

        // then
        assertFalse(tested.uiState.value.searchModeIsActive)
    }

    @Test
    fun `given location query changed and searching autocomplete options returns successful results when autocomplete address item clicked then autocomplete results are cleared`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onAddressItemClicked(index = 0)

            // then
            assertEquals(
                tested.uiState.value.locationAutocompleteAddresses,
                emptyList<String>()
            )
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful results when autocomplete address item clicked then location search query UI state is set to the address line clicked`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onAddressItemClicked(index = 0)

            // then
            assertEquals(
                tested.locationSearchQuery,
                "addressLine1"
            )
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful results when autocomplete address item clicked then address selected event is emitted`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onAddressItemClicked(index = 0)

            // then
            assertEquals(
                tested.addressSelected.value!!.peekContent()!!.address,
                addressAutocompleteResult1
            )
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful when clear location search clicked then location search query UI static is cleared`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onClearLocationSearchClicked()

            // then
            assertEquals(
                tested.locationSearchQuery,
                ""
            )
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful when clear location search clicked then address selected event is emitted with null address`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onClearLocationSearchClicked()

            // then
            assertNull(tested.addressSelected.value!!.peekContent()!!.address)
        }

    @Test
    fun `given location query changed and searching autocomplete options returns successful when clear location search clicked then autocomplete results are cleared`() =
        runTest {
            // given
            tested.onLocationSearchQueryChanged("locationQuery")
            advanceTimeBy(1100)

            // when
            tested.onClearLocationSearchClicked()

            // then
            assertEquals(
                tested.uiState.value.locationAutocompleteAddresses,
                emptyList<String>()
            )
        }
}
