package com.composeplayground.repository.search

import com.composeplayground.model.UserSearchDetails
import com.composeplayground.network.api.SearchApi
import com.composeplayground.repository.UserSearchRepository
import com.composeplayground.repository.converter.toUserSearchDetails
import javax.inject.Inject

class UserSearchRepositoryImpl @Inject constructor(private val searchApi: SearchApi) :
    UserSearchRepository {
    override suspend fun getUserList(query: String, page: Int): UserSearchDetails =
        searchApi.fetchUserList(query = query, page = page).toUserSearchDetails()
}