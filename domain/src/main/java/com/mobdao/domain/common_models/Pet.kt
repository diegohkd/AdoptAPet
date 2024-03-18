package com.mobdao.domain.common_models

data class Pet(
    val id: String,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>,
)