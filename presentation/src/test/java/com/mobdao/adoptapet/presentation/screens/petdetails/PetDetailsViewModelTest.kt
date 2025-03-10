package com.mobdao.adoptapet.presentation.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mobdao.adoptapet.common.testutils.MainDispatcherRule
import com.mobdao.adoptapet.common.testutils.domain.BreedsMockFactory
import com.mobdao.adoptapet.common.testutils.domain.ContactMockFactory
import com.mobdao.adoptapet.common.testutils.domain.PetMockFactory
import com.mobdao.adoptapet.common.testutils.domain.PhotoMockFactory
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.usecases.pets.GetCachedPetUseCase
import com.mobdao.adoptapet.presentation.common.utils.PetUtils
import com.mobdao.adoptapet.presentation.navigation.Destination
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.ContactState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetDetailsCardState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetHeaderState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PetDetailsViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val petDetailsDestination: Destination.PetDetails =
        mockk {
            every { petId } returns "pet-id"
        }
    private val description: String = "description"
    private val pet: Pet =
        PetMockFactory.create(
            name = "name",
            breeds = BreedsMockFactory.create(primary = "primary"),
            age = "age",
            size = "size",
            gender = "gender",
            description = description,
            distance = 123f,
            photos = listOf(PhotoMockFactory.create(largeUrl = "largeUrl")),
            contact =
                ContactMockFactory.create(
                    email = "email",
                    phone = "phone",
                ),
        )

    private val savedStateHandle: SavedStateHandle by lazy {
        mockk {
            every { toRoute<Destination.PetDetails>() } returns petDetailsDestination
        }
    }
    private val getCachedPetUseCase: GetCachedPetUseCase =
        mockk {
            every { execute("pet-id") } returns flowOf(pet)
        }
    private val petUtils: PetUtils =
        mockk {
            every { formattedDescriptionWorkaround(description = any()) } returns description
        }

    private val tested by lazy {
        PetDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCachedPetUseCase = getCachedPetUseCase,
            petUtils = petUtils,
        )
    }

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `given getting cached Pet throws an exception when initialized then generic error dialog is shown`() {
        // given
        every { getCachedPetUseCase.execute(any()) } returns flow { throw RuntimeException() }

        // when / then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            true,
        )
    }

    @Test
    fun `given getting cached Pet throws an exception when initialized then generic error dialog is shown and UI Pet data is not set`() {
        // given
        every { getCachedPetUseCase.execute(any()) } returns flow { throw RuntimeException() }

        // when / then
        assertEquals(
            tested.uiState.value,
            PetDetailsUiState(
                petHeader = PetHeaderState(),
                petCard = PetDetailsCardState(),
                contact = ContactState(),
                genericErrorDialogIsVisible = true,
            ),
        )
    }

    @Test
    fun `given getting cached Pet returns Pet when initialized then UI Pet data is set`() {
        // given / when / then
        assertEquals(
            tested.uiState.value,
            PetDetailsUiState(
                petHeader =
                    PetHeaderState(
                        photoUrl = "largeUrl",
                        name = "name",
                    ),
                petCard =
                    PetDetailsCardState(
                        breed = "primary",
                        age = "age",
                        gender = "gender",
                        description = "description",
                        size = "size",
                        distance = 123f,
                    ),
                contact =
                    ContactState(
                        email = "email",
                        phone = "phone",
                    ),
                genericErrorDialogIsVisible = false,
            ),
        )
    }

    @Test
    fun `given generic error dialog is shown when dismiss generic error dialog then generic error dialog is not shown`() {
        // given
        every { getCachedPetUseCase.execute(any()) } returns flow { throw RuntimeException() }
        tested

        // when
        tested.onUiAction(DismissGenericErrorDialog)

        // then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            false,
        )
    }
}
