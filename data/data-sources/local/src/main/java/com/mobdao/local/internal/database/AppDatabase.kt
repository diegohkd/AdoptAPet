package com.mobdao.local.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdao.local.internal.database.daos.AddressDao
import com.mobdao.local.internal.database.daos.AnimalDao
import com.mobdao.local.internal.database.entities.Address
import com.mobdao.local.internal.database.entities.Animal
import com.mobdao.local.internal.database.typeconverters.PhotoTypeConverter

@Database(
    entities = [Animal::class, Address::class],
    version = 1,
)
@TypeConverters(PhotoTypeConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    abstract fun animalDao(): AnimalDao
}
