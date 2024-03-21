package com.mobdao.cache.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdao.cache.database.entities.Animal

@Dao
internal interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animal: List<Animal>)

    @Query("SELECT * FROM animal WHERE id = :id")
    fun getById(id: String): Animal?
}
