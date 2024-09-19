package com.composeplayground.model


data class Repo(
    val id: Long,
    val name: String,
    val htmlUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val language: String?
)