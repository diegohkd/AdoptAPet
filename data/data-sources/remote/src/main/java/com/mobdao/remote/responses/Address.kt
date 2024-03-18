package com.mobdao.remote.responses

// TODO is it better to add this model? Or better to return the model from the google library?
data class Address(
    val addressLine: String,
    val latitude: Double,
    val longitude: Double,
)