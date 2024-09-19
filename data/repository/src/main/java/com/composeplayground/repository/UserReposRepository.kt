package com.composeplayground.repository

import com.composeplayground.model.Repo
import com.composeplayground.model.User

interface UserReposRepository {
    suspend fun getUserRepoList(userId: String, page: Int): List<Repo>
    suspend fun getUser(userId: String): User
}