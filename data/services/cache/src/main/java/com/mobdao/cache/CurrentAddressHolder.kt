package com.mobdao.cache

import com.mobdao.cache.models.Address
import javax.inject.Inject
import javax.inject.Singleton

// TODO is holder a good name?
// TODO is it better to have a single class managing caching?
@Singleton
class CurrentAddressHolder @Inject constructor() {

    // Just caching in the memory
    private var address: Address? = null

    fun set(address: Address?) {
        this.address = address
    }

    fun get(): Address? = address
}