package com.mobdao.domain_api.entitites

data class Pet(
    val id: String,
    val name: String,
    val photos: List<Photo>
)