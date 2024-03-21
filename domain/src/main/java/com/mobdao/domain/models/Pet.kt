package com.mobdao.domain.models

data class Pet(
    val id: String,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>,
)
