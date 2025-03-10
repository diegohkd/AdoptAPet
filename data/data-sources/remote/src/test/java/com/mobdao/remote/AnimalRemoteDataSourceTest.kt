package com.mobdao.remote

import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.FEMALE
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.MALE
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.DOG
import com.mobdao.remote.AnimalRemoteDataSource.GeoCoordinates
import com.mobdao.remote.internal.responses.Animal
import com.mobdao.remote.internal.responses.AnimalType
import com.mobdao.remote.internal.responses.AnimalsResponse
import com.mobdao.remote.internal.responses.Gender
import com.mobdao.remote.internal.services.PetFinderService
import com.mobdao.remote.internal.utils.mappers.EntityMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalRemoteDataSourceTest {
    private val animal1: Animal = mockk()
    private val animal2: Animal = mockk()
    private val animals = listOf(animal1, animal2)
    private val animalsResponse: AnimalsResponse =
        mockk {
            every { animals } returns this@AnimalRemoteDataSourceTest.animals
        }
    private val pet1: PetEntity = mockk()
    private val pet2: PetEntity = mockk()
    private val pets = listOf(pet1, pet2)

    private val petFinderService: PetFinderService =
        mockk {
            coEvery {
                getAnimals(
                    pageNumber = 1,
                    location = "-123.0,456.0",
                    type = "Dog",
                    gender = "male,female",
                )
            } returns animalsResponse
        }
    private val entityMapper: EntityMapper =
        mockk {
            every { toPets(animals) } returns pets
            every { toAnimalType(DOG) } returns AnimalType.DOG
            every { toGender(MALE) } returns Gender.MALE
            every { toGender(FEMALE) } returns Gender.FEMALE
        }

    private val tested = AnimalRemoteDataSource(petFinderService, entityMapper)

    @Test
    fun `given page number, coordinates and animal type when get pets then pets are returned`() =
        runTest {
            // given / when
            val result: List<PetEntity> =
                tested.getPets(
                    pageNumber = 1,
                    locationCoordinates =
                        GeoCoordinates(
                            latitude = -123.0,
                            longitude = 456.0,
                        ),
                    petType = DOG,
                    petGenders = listOf(MALE, FEMALE),
                )

            // then
            assertEquals(result, pets)
        }
}
