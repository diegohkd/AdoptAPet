package com.mobdao.data.cache.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdao.data.cache.database.entities.Animal

@Dao
interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg animal: Animal)

    @Query("SELECT * FROM animal WHERE id = :id")
    fun getById(id: String): Animal
}