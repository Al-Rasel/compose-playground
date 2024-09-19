package com.composeplayground.domain.usecase

import com.composeplayground.model.UserSearchDetails
import com.composeplayground.repository.UserSearchRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(private val searchRepository: UserSearchRepository) {
    suspend fun search(searchQuery: String, page: Int): UserSearchDetails =
        searchRepository.getUserList(query = searchQuery, page = page)
}