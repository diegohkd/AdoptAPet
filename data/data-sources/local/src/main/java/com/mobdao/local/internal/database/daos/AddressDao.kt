package com.mobdao.local.internal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdao.local.internal.database.entities.AddressDbModel

@Dao
internal interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(address: List<AddressDbModel>)

    @Query("SELECT * FROM address")
    fun getAll(): List<AddressDbModel>

    @Query("DELETE FROM address")
    fun nukeTable()
}
