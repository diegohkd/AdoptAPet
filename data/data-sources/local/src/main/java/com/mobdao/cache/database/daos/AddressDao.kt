package com.mobdao.cache.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdao.cache.database.entities.Address

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(address: List<Address>)

    @Query("SELECT * FROM address")
    fun getAll(): List<Address>

    @Query("DELETE FROM address")
    fun nukeTable()
}