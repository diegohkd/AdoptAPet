package com.mobdao.adoptapet.presentation.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.usecases.pets.GetCachedPetUseCase
import com.mobdao.adoptapet.presentation.navigation.Destination.PetDetails.PET_ID_ARG
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.ContactState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetCardState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetHeaderState
import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.common.testutils.mockfactories.domain.BreedsMockFactory
import com.mobdao.common.testutils.mockfactories.domain.ContactMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PetMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PhotoMockFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PetDetailsViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val petId: String = "pet-id"
    private val pet: Pet =
        PetMockFactory.create(
            name = "name",
            breeds = BreedsMockFactory.create(primary = "primary"),
            age = "age",
            size = "size",
            gender = "gender",
            description = "description",
            distance = 123f,
            photos = listOf(PhotoMockFactory.create(largeUrl = "largeUrl")),
            contact =
                ContactMockFactory.create(
                    email = "email",
                    phone = "phone",
                ),
        )

    private val savedStateHandle: SavedStateHandle =
        mockk {
            every { get<String>(PET_ID_ARG) } returns petId
        }
    private val getCachedPetUseCase: GetCachedPetUseCase =
        mockk {
            every { execute("pet-id") } returns flowOf(pet)
        }

    private val tested by lazy {
        PetDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCachedPetUseCase = getCachedPetUseCase,
        )
    }

    @Test
    fun `given pet ID is null when initialized then generic error dialog is shown`() {
        // given
        every { savedStateHandle.get<String>(PET_ID_ARG) } returns null

        // when / then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            true,
        )
    }

    @Test
    fun `given pet ID not null when initialized then generic error dialog is not shown`() {
        // given / when / then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            false,
        )
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
                petCard = PetCardState(),
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
                    PetCardState(
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
        every { savedStateHandle.get<String>(PET_ID_ARG) } returns null
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
