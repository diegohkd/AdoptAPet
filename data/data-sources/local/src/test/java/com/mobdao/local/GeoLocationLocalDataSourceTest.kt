package com.mobdao.local

import com.mobdao.local.internal.common.DomainEntityAddress
import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AddressDao
import com.mobdao.local.internal.database.entities.Address
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GeoLocationLocalDataSourceTest {

    private val address1: DomainEntityAddress = mockk()
    private val dbAddress1: Address = mockk()
    private val dbAddress2: Address = mockk()

    private val addressDao: AddressDao = mockk {
        coJustRun { nukeTable() }
        coJustRun { insertAll(listOf(dbAddress1)) }
        coEvery { getAll() } returns listOf(dbAddress1, dbAddress2)
    }
    private val entityMapper: EntityMapper = mockk {
        every { toDbEntity(address1) } returns dbAddress1
        every { toDomainEntity(dbAddress1) } returns address1
    }

    private val tested = GeoLocationLocalDataSource(addressDao, entityMapper)

    @Test
    fun `given address when save as current address then address table is cleared and address is inserted`() =
        runTest {
            // given / when
            tested.saveCurrentAddress(address1)

            // then
            coVerifyOrder {
                addressDao.nukeTable()
                addressDao.insertAll(listOf(dbAddress1))
            }
        }

    @Test
    fun `when get current address then current address is returned`() = runTest {
        // when
        val result: DomainEntityAddress? = tested.getCurrentAddress()

        // then
        assertEquals(result, address1)
    }
}
