package com.composeplayground.repository.repos

import com.composeplayground.model.Repo
import com.composeplayground.model.User
import com.composeplayground.network.api.SearchApi
import com.composeplayground.repository.UserReposRepository
import com.composeplayground.repository.converter.toRepo
import com.composeplayground.repository.converter.toUser
import javax.inject.Inject

class UserReposRepositoryImpl @Inject constructor(private val searchApi: SearchApi) :
    UserReposRepository {
    override suspend fun getUserRepoList(userId: String, page: Int): List<Repo> =
        searchApi.fetchRepoList(
            query = "user:$userId",
            type = "repositories",
            page = page
        ).repos.map { it.toRepo() }

    override suspend fun getUser(userId: String): User =
        searchApi.fetchUser(userId = userId).toUser()
}