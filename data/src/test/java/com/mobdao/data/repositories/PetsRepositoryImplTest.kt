package com.mobdao.data.repositories

import com.mobdao.cache.AnimalLocalDataSource
import com.mobdao.common.testutils.mockfactories.data.local.AnimalDbEntityMockFactory
import com.mobdao.common.testutils.mockfactories.domain.dataapi.entities.AddressEntityMockFactory
import com.mobdao.common.testutils.mockfactories.domain.dataapi.entities.SearchFilterEntityMockFactory
import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.utils.mappers.AnimalMapper
import com.mobdao.domain.dataapi.entitites.Pet
import com.mobdao.domain.dataapi.entitites.SearchFilter
import com.mobdao.remote.AnimalRemoteDataSource
import com.mobdao.remote.AnimalRemoteDataSource.GeoCoordinates
import com.mobdao.remote.responses.Animal
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PetsRepositoryImplTest {

    private val animal1: Animal = mockk()
    private val animal2: Animal = mockk()
    private val address = AddressEntityMockFactory.create(
        latitude = 123.0,
        longitude = 123.0,
    )
    private val searchFilter: SearchFilter = SearchFilterEntityMockFactory.create(
        address = address,
        petType = "petType",
    )
    private val animalDbEntity1: AnimalDbEntity = AnimalDbEntityMockFactory.create(id = "id-1")
    private val animalDbEntity2: AnimalDbEntity = mockk()
    private val pet1: Pet = mockk()
    private val pet2: Pet = mockk()

    private val animalRemoteDataSource: AnimalRemoteDataSource = mockk {
        coEvery {
            getAnimals(
                pageNumber = 1,
                locationCoordinates = GeoCoordinates(
                    latitude = 123.0,
                    longitude = 123.0
                ),
                animalType = "petType",
            )
        } returns listOf(animal1, animal2)
    }
    private val animalLocalDataSource: AnimalLocalDataSource = mockk {
        coJustRun { saveAnimals(listOf(animalDbEntity1, animalDbEntity2)) }
        coEvery { getAnimalById(animalId = "id-1") } returns animalDbEntity1
    }
    private val animalMapper: AnimalMapper = mockk {
        every { mapToDbEntity(listOf(animal1, animal2)) } returns listOf(
            animalDbEntity1,
            animalDbEntity2
        )
        every { mapToPet(listOf(animal1, animal2)) } returns listOf(pet1, pet2)
        every { mapToPet(animalDbEntity1) } returns pet1
    }

    private val tested = PetsRepositoryImpl(
        animalRemoteDataSource = animalRemoteDataSource,
        animalLocalDataSource = animalLocalDataSource,
        animalMapper = animalMapper,
    )

    @Test
    fun `given search filter when get pets then fetched animals are cached`() = runTest {
        // given / when
        tested.getPets(
            pageNumber = 1,
            searchFilter = searchFilter,
        )

        // then
        coVerify { animalLocalDataSource.saveAnimals(listOf(animalDbEntity1, animalDbEntity2)) }
    }

    @Test
    fun `given search filter when get pets then pets are returned`() = runTest {
        // given / when
        val result: List<Pet> = tested.getPets(
            pageNumber = 1,
            searchFilter = searchFilter,
        )

        // then
        assertEquals(result, listOf(pet1, pet2))
    }

    @Test
    fun `given unknown Pet ID when get Pet by ID then null is returned`() = runTest {
        // given
        coEvery { animalLocalDataSource.getAnimalById("unknown-id") } returns null

        // when
        val result: Pet? = tested.getCachedPetById(petId = "unknown-id")

        // then
        assertNull(result)
    }

    @Test
    fun `given known Pet ID when get Pet by ID then Pet with same ID is returned`() = runTest {
        // given / when
        val result: Pet? = tested.getCachedPetById(petId = "id-1")

        // then
        assertEquals(result, pet1)
    }
}