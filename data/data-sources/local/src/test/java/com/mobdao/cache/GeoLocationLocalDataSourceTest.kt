package com.mobdao.cache

import com.mobdao.cache.database.daos.AddressDao
import com.mobdao.cache.database.entities.Address
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GeoLocationLocalDataSourceTest {

    private val address1: Address = mockk()
    private val address2: Address = mockk()

    private val addressDao: AddressDao = mockk {
        coJustRun { nukeTable() }
        coJustRun { insertAll(listOf(address1)) }
        coEvery { getAll() } returns listOf(address1, address2)
    }

    private val tested = GeoLocationLocalDataSource(addressDao)

    @Test
    fun `given address when save as current address then address table is cleared and address is inserted`() =
        runTest {
            // given / when
            tested.saveCurrentAddress(address1)

            // then
            coVerifyOrder {
                addressDao.nukeTable()
                addressDao.insertAll(listOf(address1))
            }
        }

    @Test
    fun `when get current address then current address is returned`() = runTest {
        // when
        val result: Address? = tested.getCurrentAddress()

        // then
        assertEquals(result, address1)
    }
}