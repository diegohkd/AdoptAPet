package com.mobdao.local

import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AnimalDao
import com.mobdao.local.internal.database.entities.Animal
import com.mobdao.local.mockfactories.AnimalDbEntityMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.PetEntityMockFactory
import com.mobdao.domain.entities.Pet
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalLocalDataSourceTest {

    private val pet1: Pet = PetEntityMockFactory.create()
    private val pet2: Pet = PetEntityMockFactory.create()
    private val pets = listOf(pet1, pet2)
    private val animal1: Animal = AnimalDbEntityMockFactory.create()
    private val animal2: Animal = AnimalDbEntityMockFactory.create()
    private val animals = listOf(animal1, animal2)

    private val animalDao: AnimalDao = mockk {
        coJustRun { insertAll(animals) }
        coEvery { getById(id = "id") } returns animal1
    }
    private val entityMapper: EntityMapper = mockk {
        coEvery { toAnimal(pets) } returns animals
        coEvery { toPet(animal1) } returns pet1
    }

    private val tested = AnimalLocalDataSource(animalDao, entityMapper)

    @Test
    fun `given pets when save pets then they are inserted into DB`() = runTest {
        // given / when
        tested.savePets(pets)

        // then
        coVerify { animalDao.insertAll(animals) }
    }

    @Test
    fun `given pet ID when get pet by ID then pet is retrieved from DB`() = runTest {
        // given / when
        val result: Pet? = tested.getPetById(petId = "id")

        // then
        assertEquals(result, pet1)
    }
}