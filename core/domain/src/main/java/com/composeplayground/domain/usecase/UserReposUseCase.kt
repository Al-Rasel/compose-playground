package com.composeplayground.domain.usecase

import com.composeplayground.model.Repo
import com.composeplayground.model.User
import com.composeplayground.repository.UserReposRepository
import javax.inject.Inject

class UserReposUseCase @Inject constructor(private val userReposRepository: UserReposRepository) {
    suspend fun userRepos(userId: String, page: Int): List<Repo> =
        userReposRepository.getUserRepoList(userId = userId, page = page)

    suspend fun userDetails(userId: String): User = userReposRepository.getUser(userId)
}