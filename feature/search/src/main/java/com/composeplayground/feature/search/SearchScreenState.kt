package com.composeplayground.feature.search

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.composeplayground.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Stable
data class SearchScreenState(
    val searchQuery: String = "",
    val userList: Flow<PagingData<User>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = true
)