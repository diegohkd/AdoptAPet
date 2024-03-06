package com.mobdao.domain.common_models

data class Pet(
    val id: String,
    val name: String,
    val photos: List<Photo>,
)