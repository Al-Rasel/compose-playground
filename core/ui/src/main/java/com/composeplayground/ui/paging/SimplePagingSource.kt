package com.composeplayground.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SimplePagingSource<T : Any>(
    private val getSearchData: suspend (Int) -> List<T>,
    private val onError: (Exception) -> Unit
) : PagingSource<Int, T>() {

    companion object {
        const val PAGE_SIZE = 30
        const val GITHUB_STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
            val items: List<T> = getSearchData(position)
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = items,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            onError(e)
            LoadResult.Error(e)
        }
    }
}
