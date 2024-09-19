package com.composeplayground.feature.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.composeplayground.feature.details.navigation.DetailsRoute
import com.composeplayground.domain.usecase.UserReposUseCase
import com.composeplayground.ui.action.ResultAction
import com.composeplayground.ui.action.ShowToastMessage
import com.composeplayground.ui.extensions.PagingItemLoadStates
import com.composeplayground.ui.paging.SimplePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val userReposUseCase: UserReposUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val detailsRoute = state.toRoute<DetailsRoute>()
    private val _state = MutableStateFlow(
        DetailsScreenState(
            userName = detailsRoute.userId,
            userFullName = detailsRoute.userFullName,
            iconUrl = detailsRoute.iconUrl,
            followersCount = detailsRoute.followersCount,
            followingCount = detailsRoute.followingCount
        )
    )
    val state = _state

    private val _resultAction = Channel<ResultAction>()
    val resultAction = _resultAction.receiveAsFlow()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // todo: handle it properly, for now just a log
        Log.e("DetailsViewModel", "${throwable.message}")
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            val userUpdateData = userReposUseCase.userDetails(userId = detailsRoute.userId)
            _state.update {
                it.copy(
                    userFullName = userUpdateData.name,
                    iconUrl = userUpdateData.imageUrl,
                    followersCount = userUpdateData.followers,
                    followingCount = userUpdateData.following
                )
            }
        }
    }

    val repositoryList = Pager(
        PagingConfig(
            pageSize = SimplePagingSource.PAGE_SIZE,
            prefetchDistance = 20,
            initialLoadSize = SimplePagingSource.PAGE_SIZE

        )
    ) {
        SimplePagingSource(
            getSearchData = { page ->
                userReposUseCase.userRepos(detailsRoute.userId, page)
            },
            onError = { e ->
                // TODO: handle error for different types
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
}