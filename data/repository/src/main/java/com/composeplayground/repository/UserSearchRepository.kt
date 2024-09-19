package com.composeplayground.repository

import com.composeplayground.model.UserSearchDetails


interface UserSearchRepository {
    suspend fun getUserList(query: String, page: Int): UserSearchDetails
}