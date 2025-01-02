package com.mobdao.domain.usecases.pets

import com.mobdao.adoptapet.domain.dataapi.repositories.PetsRepository
import com.mobdao.common.testutils.mockfactories.domain.PetMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.PetEntityMockFactory
import com.mobdao.domain.internal.PetEntity
import com.mobdao.domain.internal.mappers.PetMapper
import com.mobdao.domain.models.Pet
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCachedPetUseCaseTest {
    private val petId: String = "petId"
    private val petEntity: PetEntity = PetEntityMockFactory.create()
    private val pet: Pet = PetMockFactory.create()

    private val petsRepository: PetsRepository =
        mockk {
            coEvery { getCachedPetById(petId) } returns petEntity
        }
    private val petMapper: PetMapper =
        mockk {
            every { map(petEntity) } returns pet
        }

    private val tested =
        GetCachedPetUseCase(
            petsRepository = petsRepository,
            petMapper = petMapper,
        )

    @Test
    fun `given Pet ID when execute then it returns cached by with same ID`() =
        runTest {
            // given / when
            val result: Pet? = tested.execute(petId).first()

            // then
            assertEquals(result, pet)
        }
}
