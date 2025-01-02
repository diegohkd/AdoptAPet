package com.mobdao.local.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdao.local.internal.database.daos.AddressDao
import com.mobdao.local.internal.database.daos.AnimalDao
import com.mobdao.local.internal.database.entities.AddressDbModel
import com.mobdao.local.internal.database.entities.AnimalDbModel
import com.mobdao.local.internal.database.typeconverters.PhotoTypeConverter

@Database(
    entities = [AnimalDbModel::class, AddressDbModel::class],
    version = 1,
)
@TypeConverters(PhotoTypeConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    abstract fun animalDao(): AnimalDao
}
