package com.mobdao.domain.models

data class Pet(
    val id: String,
    val name: String,
    val photos: List<Photo>,
)