package com.composeplayground.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.composeplayground.domain.usecase.SearchUsersUseCase
import com.composeplayground.ui.action.ResultAction
import com.composeplayground.ui.action.ShowToastMessage
import com.composeplayground.ui.extensions.PagingItemLoadStates
import com.composeplayground.ui.paging.SimplePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUsersUseCase: SearchUsersUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(
        SearchScreenState()
    )
    val state = _state
    private val searchQueryFlow = MutableStateFlow("")

    private val _resultAction = Channel<ResultAction>()
    val resultAction = _resultAction.receiveAsFlow()

    init {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(500)
                .distinctUntilChanged()
                .map { it.ifEmpty { "a" } } // just put an initial value to avoid empty screen
                .collect { query ->
                    val userListFlow = usersListPagingFlow(query)
                    _state.update {
                        it.copy(
                            userList = userListFlow
                        )
                    }
                }
        }
    }

    fun handleLoadState(loadState: PagingItemLoadStates) {
        _state.update {
            it.copy(
                isLoading = when {
                    loadState.isError() || loadState.isEndOfPaginationReached() -> false
                    else -> loadState.isLoading()
                }
            )
        }
    }

    fun onQueryChange(query: String) {
        _state.update {
            it.copy(
                searchQuery = query
            )
        }
        if (query.isEmpty()) {
            _state.update {
                it.copy(
                    userList = flowOf(PagingData.empty())
                )
            }
        }
        searchQueryFlow.value = query
    }

    private fun usersListPagingFlow(query: String) = Pager(
        PagingConfig(
            pageSize = SimplePagingSource.PAGE_SIZE,
            prefetchDistance = 20,
            initialLoadSize = SimplePagingSource.PAGE_SIZE
        )
    ) {
        SimplePagingSource(
            getSearchData = { page ->
                searchUsersUseCase.search(query, page).users
            },
            onError = { e ->
                // TODO: handle error in details, for different types
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
                viewModelScope.launch {
                    _resultAction.send(ShowToastMessage(e.message ?: "$e"))
                }
            }
        )
    }.flow.cachedIn(viewModelScope)
}