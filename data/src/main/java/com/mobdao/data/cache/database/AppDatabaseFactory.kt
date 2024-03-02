package com.mobdao.data.cache.database

import android.content.Context
import androidx.room.Room
import com.mobdao.data.cache.database.type_converters.PhotoTypeConverter
import javax.inject.Inject
import javax.inject.Singleton

private const val DB_NAME = "adopt-a-pet-database"

@Singleton
class AppDatabaseFactory @Inject constructor(private val photoTypeConverter: PhotoTypeConverter) {

    fun create(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .addTypeConverter(photoTypeConverter)
            .build()
}