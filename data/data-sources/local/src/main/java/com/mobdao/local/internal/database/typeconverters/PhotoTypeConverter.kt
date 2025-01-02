package com.mobdao.local.internal.database.typeconverters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mobdao.adoptapet.common.JsonAdapter
import com.mobdao.local.internal.database.entities.Photo
import javax.inject.Inject
import javax.inject.Singleton

// TODO save Photo as another database entity
@Singleton
@ProvidedTypeConverter
internal class PhotoTypeConverter
    @Inject
    constructor(
        private val jsonAdapter: JsonAdapter,
    ) {
        @TypeConverter
        fun fromPhotos(data: List<Photo>): String = jsonAdapter.toJson(PhotosList(data), PhotosList::class.java)

        @TypeConverter
        fun toPhotos(json: String): List<Photo> = jsonAdapter.fromJson(json, PhotosList::class.java)!!.photos
    }

private data class PhotosList(
    val photos: List<Photo>,
)
