package com.mobdao.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.database.entities.Animal
import com.mobdao.cache.database.type_converters.PhotoTypeConverter

@Database(
    entities = [Animal::class],
    version = 1
)
@TypeConverters(PhotoTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao
}