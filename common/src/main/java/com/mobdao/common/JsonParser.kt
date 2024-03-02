package com.mobdao.common

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

// TODO find better name
@Suppress("UNCHECKED_CAST")
@Singleton
class JsonAdapter @Inject constructor(private val moshi: Moshi) {

    private val adaptersMap: MutableMap<Class<*>, JsonAdapter<*>> = mutableMapOf()

    fun <T> fromJson(json: String, type: Class<T>): T? = getAdapter(type).fromJson(json)
    fun <T> toJson(obj: T, type: Class<T>): String = getAdapter(type).toJson(obj)

    private fun <T> getAdapter(type: Class<T>): JsonAdapter<T> =
        adaptersMap.getOrPut(type) { moshi.adapter(type) } as JsonAdapter<T>
}