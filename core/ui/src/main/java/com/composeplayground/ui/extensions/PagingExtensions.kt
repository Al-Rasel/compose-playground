package com.composeplayground.ui.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun LazyPagingItems<*>.rememberLazyPagingListState(): LazyListState {

    return when (itemCount) {
        0 -> remember(this) { LazyListState(0, 0) }
        else -> rememberLazyListState()
    }
}

fun LazyPagingItems<*>.pendingLoadStates(): PagingItemLoadStates {
    return PagingItemLoadStates(
        append = loadState.append,
        prepend = loadState.prepend
    )
}

data class PagingItemLoadStates(
    val append: LoadState,
    val prepend: LoadState,
) {

    fun isError(): Boolean {
        return append is LoadState.Error || prepend is LoadState.Error
    }

    fun isEndOfPaginationReached(): Boolean {
        return append.endOfPaginationReached || prepend.endOfPaginationReached
    }

    fun isLoading(): Boolean {
        return append.endOfPaginationReached.not() && prepend.endOfPaginationReached.not()
    }
}
