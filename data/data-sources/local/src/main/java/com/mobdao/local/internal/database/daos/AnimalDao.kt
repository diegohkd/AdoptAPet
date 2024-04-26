package com.mobdao.local.internal.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdao.local.internal.database.entities.Animal

@Dao
internal interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animal: List<Animal>)

    @Query("SELECT * FROM animal WHERE id = :id")
    fun getById(id: String): Animal?
}
