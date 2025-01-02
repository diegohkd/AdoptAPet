package com.mobdao.domain.usecases.pets

import com.mobdao.adoptapet.domain.dataapi.repositories.PetsRepository
import com.mobdao.adoptapet.domain.internal.PetEntity
import com.mobdao.adoptapet.domain.internal.SearchFilterEntity
import com.mobdao.adoptapet.domain.internal.mappers.PetMapper
import com.mobdao.adoptapet.domain.internal.mappers.SearchFilterMapper
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.domain.usecases.pets.GetPetsUseCase
import com.mobdao.common.testutils.mockfactories.domain.PetMockFactory
import com.mobdao.common.testutils.mockfactories.domain.SearchFilterMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.PetEntityMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.SearchFilterEntityMockFactory
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPetsUseCaseTest {
    private val pageNumber: Int = 1
    private val searchFilter: SearchFilter = SearchFilterMockFactory.create()
    private val searchFilterEntity: SearchFilterEntity = SearchFilterEntityMockFactory.create()
    private val petEntity1: PetEntity = PetEntityMockFactory.create()
    private val petEntity2: PetEntity = PetEntityMockFactory.create()
    private val pet1: Pet = PetMockFactory.create()
    private val pet2: Pet = PetMockFactory.create()

    private val petsRepository: PetsRepository =
        mockk {
            coEvery {
                getPets(
                    pageNumber = pageNumber,
                    searchFilter = searchFilterEntity,
                )
            } returns listOf(petEntity1, petEntity2)
        }
    private val petMapper: PetMapper =
        mockk {
            every { map(petEntity1) } returns pet1
            every { map(petEntity2) } returns pet2
        }
    private val searchFilterMapper: SearchFilterMapper =
        mockk {
            every { mapToEntity(searchFilter) } returns searchFilterEntity
        }

    private val tested =
        GetPetsUseCase(
            petsRepository = petsRepository,
            petMapper = petMapper,
            searchFilterMapper = searchFilterMapper,
        )

    @Test
    fun `when execute then list of Pets is returned`() =
        runTest {
            // given / when
            val result = tested.execute(pageNumber, searchFilter).first()

            // then
            assertEquals(
                result,
                listOf(pet1, pet2),
            )
        }
}
