package com.mobdao.local

import com.mobdao.adoptapet.common.testutils.domain.entities.PetEntityMockFactory
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AnimalDao
import com.mobdao.local.internal.database.entities.AnimalDbModel
import com.mobdao.local.mockfactories.AnimalDbEntityMockFactory
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalDbModelLocalDataSourceTest {
    private val pet1: PetEntity = PetEntityMockFactory.create()
    private val pet2: PetEntity = PetEntityMockFactory.create()
    private val pets = listOf(pet1, pet2)
    private val animal1: AnimalDbModel = AnimalDbEntityMockFactory.create()
    private val animal2: AnimalDbModel = AnimalDbEntityMockFactory.create()
    private val animals = listOf(animal1, animal2)

    private val animalDao: AnimalDao =
        mockk {
            coJustRun { insertAll(animals) }
            coEvery { getById(id = "id") } returns animal1
        }
    private val entityMapper: EntityMapper =
        mockk {
            coEvery { toDbModel(pets) } returns animals
            coEvery { toEntity(animal1) } returns pet1
        }

    private val tested = AnimalLocalDataSource(animalDao, entityMapper)

    @Test
    fun `given pets when save pets then they are inserted into DB`() =
        runTest {
            // given / when
            tested.savePets(pets)

            // then
            coVerify { animalDao.insertAll(animals) }
        }

    @Test
    fun `given pet ID when get pet by ID then pet is retrieved from DB`() =
        runTest {
            // given / when
            val result: PetEntity? = tested.getPetById(petId = "id")

            // then
            assertEquals(result, pet1)
        }
}
