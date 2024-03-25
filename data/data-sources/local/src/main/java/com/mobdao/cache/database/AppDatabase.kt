package com.mobdao.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdao.cache.database.daos.AddressDao
import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.database.entities.Address
import com.mobdao.cache.database.entities.Animal
import com.mobdao.cache.database.typeconverters.PhotoTypeConverter

@Database(
    entities = [Animal::class, Address::class],
    version = 1
)
@TypeConverters(PhotoTypeConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
    abstract fun animalDao(): AnimalDao
}
