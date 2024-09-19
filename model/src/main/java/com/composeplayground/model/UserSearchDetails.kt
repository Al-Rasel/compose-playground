package com.composeplayground.model

data class UserSearchDetails(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val users: List<User>
)