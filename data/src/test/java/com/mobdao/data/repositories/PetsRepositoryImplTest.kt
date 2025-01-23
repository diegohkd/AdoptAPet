package com.mobdao.data.repositories

import com.mobdao.adoptapet.common.testutils.domain.entities.AddressEntityMockFactory
import com.mobdao.adoptapet.common.testutils.domain.entities.SearchFilterEntityMockFactory
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.MALE
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.DOG
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import com.mobdao.local.AnimalLocalDataSource
import com.mobdao.remote.AnimalRemoteDataSource
import com.mobdao.remote.AnimalRemoteDataSource.GeoCoordinates
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PetsRepositoryImplTest {
    private val address =
        AddressEntityMockFactory.create(
            latitude = 123.0,
            longitude = 123.0,
        )
    private val searchFilter: SearchFilterEntity =
        SearchFilterEntityMockFactory.create(
            address = address,
            petType = DOG,
            petGenders = listOf(MALE),
        )
    private val pet1: PetEntity = mockk()
    private val pet2: PetEntity = mockk()
    private val pets = listOf(pet1, pet2)

    private val animalRemoteDataSource: AnimalRemoteDataSource =
        mockk {
            coEvery {
                getPets(
                    pageNumber = 1,
                    locationCoordinates =
                        GeoCoordinates(
                            latitude = 123.0,
                            longitude = 123.0,
                        ),
                    petType = DOG,
                    petGenders = listOf(MALE),
                )
            } returns pets
        }
    private val animalLocalDataSource: AnimalLocalDataSource =
        mockk {
            coJustRun { savePets(pets) }
            coEvery { getPetById(petId = "id-1") } returns pet1
        }

    private val tested =
        PetsRepositoryImpl(
            animalRemoteDataSource = animalRemoteDataSource,
            animalLocalDataSource = animalLocalDataSource,
        )

    @Test
    fun `given search filter when get pets then fetched pets are cached`() =
        runTest {
            // given / when
            tested.getPets(
                pageNumber = 1,
                searchFilter = searchFilter,
            )

            // then
            coVerify { animalLocalDataSource.savePets(pets) }
        }

    @Test
    fun `given search filter when get pets then pets are returned`() =
        runTest {
            // given / when
            val result: List<PetEntity> =
                tested.getPets(
                    pageNumber = 1,
                    searchFilter = searchFilter,
                )

            // then
            assertEquals(result, pets)
        }

    @Test
    fun `given unknown Pet ID when get Pet by ID then null is returned`() =
        runTest {
            // given
            coEvery { animalLocalDataSource.getPetById("unknown-id") } returns null

            // when
            val result: PetEntity? = tested.getCachedPetById(petId = "unknown-id")

            // then
            assertNull(result)
        }

    @Test
    fun `given known Pet ID when get Pet by ID then Pet with same ID is returned`() =
        runTest {
            // given / when
            val result: PetEntity? = tested.getCachedPetById(petId = "id-1")

            // then
            assertEquals(result, pet1)
        }
}
