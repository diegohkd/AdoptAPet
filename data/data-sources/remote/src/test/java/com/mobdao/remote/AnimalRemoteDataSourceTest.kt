package com.mobdao.remote

import com.mobdao.remote.AnimalRemoteDataSource.GeoCoordinates
import com.mobdao.remote.responses.Animal
import com.mobdao.remote.responses.AnimalsResponse
import com.mobdao.remote.services.PetFinderService
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
    private val animalsResponse: AnimalsResponse = mockk {
        every { animals } returns this@AnimalRemoteDataSourceTest.animals
    }

    private val petFinderService: PetFinderService = mockk {
        coEvery {
            getAnimals(
                pageNumber = 1,
                location = "-123.0,456.0",
                type = "animalType",
            )
        } returns animalsResponse
    }

    private val tested = AnimalRemoteDataSource(petFinderService)

    @Test
    fun `given page number, coordinates and animal type when get animals then animals are returned`() =
        runTest {
            // given / when
            val result = tested.getAnimals(
                pageNumber = 1,
                locationCoordinates = GeoCoordinates(
                    latitude = -123.0,
                    longitude = 456.0
                ),
                animalType = "animalType",
            )

            // then
            assertEquals(result, animals)
        }
}