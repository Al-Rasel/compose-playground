package com.composeplayground.model

data class User(
    val login: String,
    val name: String?,
    val imageUrl: String,
    val followers: Int,
    val following: Int
)