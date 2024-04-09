package com.mobdao.cache

import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.database.entities.Animal
import com.mobdao.common.testutils.mockfactories.data.local.AnimalDbEntityMockFactory
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalLocalDataSourceTest {

    private val animal1: Animal = AnimalDbEntityMockFactory.create()
    private val animal2: Animal = AnimalDbEntityMockFactory.create()
    private val animals = listOf(animal1, animal2)

    private val animalDao: AnimalDao = mockk {
        coJustRun { insertAll(animals) }
        coEvery { getById(id = "id") } returns animal1
    }

    private val tested = AnimalLocalDataSource(animalDao)

    @Test
    fun `given animals when save animals then they are inserted into DB`() = runTest {
        // given / when
        tested.saveAnimals(animals)

        // then
        coVerify { animalDao.insertAll(animals) }
    }

    @Test
    fun `given animal ID when get animal by ID then animal is retrieved from DB`() = runTest {
        // given / when
        val result: Animal? = tested.getAnimalById(animalId = "id")

        // then
        assertEquals(result, animal1)
    }
}