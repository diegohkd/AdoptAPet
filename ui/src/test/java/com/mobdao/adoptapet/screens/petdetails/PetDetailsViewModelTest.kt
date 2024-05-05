package com.mobdao.adoptapet.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import com.mobdao.adoptapet.navigation.Destination.PetDetails.PET_ID_ARG
import com.mobdao.adoptapet.screens.petdetails.PetDetailsViewModel.UiState
import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.common.testutils.mockfactories.domain.PetMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PhotoMockFactory
import com.mobdao.domain.models.Pet
import com.mobdao.domain.usecases.pets.GetCachedPetUseCase
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
    private val pet: Pet = PetMockFactory.create(
        name = "name",
        photos = listOf(PhotoMockFactory.create(largeUrl = "largeUrl"))
    )

    private val savedStateHandle: SavedStateHandle = mockk {
        every { get<String>(PET_ID_ARG) } returns petId
    }
    private val getCachedPetUseCase: GetCachedPetUseCase = mockk {
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
            true
        )
    }

    @Test
    fun `given pet ID not null when initialized then generic error dialog is not shown`() {
        // given / when / then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            false
        )
    }

    @Test
    fun `given getting cached Pet throws an exception when initialized then generic error dialog is shown`() {
        // given
        every { getCachedPetUseCase.execute(any()) } returns flow { throw RuntimeException() }

        // when / then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            true
        )
    }

    @Test
    fun `given getting cached Pet throws an exception when initialized then generic error dialog is shown and UI Pet data is set`() {
        // given
        every { getCachedPetUseCase.execute(any()) } returns flow { throw RuntimeException() }

        // when / then
        assertEquals(
            tested.uiState.value,
            UiState(
                petName = "",
                photoUrl = "",
                genericErrorDialogIsVisible = true,
            )
        )
    }

    @Test
    fun `given getting cached Pet returns Pet when initialized then UI Pet data is set`() {
        // given / when / then
        assertEquals(
            tested.uiState.value,
            UiState(
                petName = "name",
                photoUrl = "largeUrl",
                genericErrorDialogIsVisible = false,
            )
        )
    }

    @Test
    fun `given generic error dialog is shown when dismiss generic error dialog then generic error dialog is not shown`() {
        // given
        every { savedStateHandle.get<String>(PET_ID_ARG) } returns null
        tested

        // when
        tested.onDismissGenericErrorDialog()

        // then
        assertEquals(
            tested.uiState.value.genericErrorDialogIsVisible,
            false
        )
    }
}
