package com.composeplayground.feature.search

import androidx.paging.testing.asSnapshot
import com.composeplayground.domain.usecase.SearchUsersUseCase
import com.composeplayground.model.UserSearchDetails
import com.composeplayground.repository.UserSearchRepository
import com.composeplayground.testing.userSearchDetails
import com.composeplayground.ui.extensions.PagingItemLoadStates
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SearchViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var pagingItemLoadStates: PagingItemLoadStates

    private lateinit var viewModel: SearchViewModel

    private val searchRepository = object :
        UserSearchRepository {
        override suspend fun getUserList(query: String, page: Int): UserSearchDetails {
            return userSearchDetails
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(
            searchUsersUseCase = SearchUsersUseCase(searchRepository = searchRepository)
        )
    }


    @Test
    fun `loading should be true when initially`() = runTest {
        assertEquals(true, viewModel.state.value.isLoading)
    }

    @Test
    fun `loading should be false when error occurs`() {
        every { pagingItemLoadStates.isError() } returns true
        viewModel.handleLoadState(pagingItemLoadStates)
        assertFalse(viewModel.state.value.isLoading)
    }


    @Test
    fun `loading should be true when on load true`() {
        every { pagingItemLoadStates.isError() } returns false
        every { pagingItemLoadStates.isEndOfPaginationReached() } returns false
        every { pagingItemLoadStates.isLoading() } returns true
        viewModel.handleLoadState(pagingItemLoadStates)
        assertEquals(true, viewModel.state.value.isLoading)
    }

    @Test
    fun `when on query change called state should be updated with updated query`() {
        viewModel.onQueryChange("testquery")
        assertEquals("testquery", viewModel.state.value.searchQuery)
    }

    @Test
    fun `when on query is empty user search list should be empty`() = runTest {
        viewModel.onQueryChange("")
        val resultSize = viewModel.state.value.userList.asSnapshot().size
        assertEquals(resultSize, 0)
    }

    @Test
    fun `when query is not empty users size will be 5`() = runTest {
        val mockRepo = mockk<UserSearchRepository>()
        coEvery {
            mockRepo.getUserList("query", 1)
        } coAnswers {
            userSearchDetails // First call
        }
        // paging will try to call second time so ti should be empty that time
        coEvery {
            mockRepo.getUserList("query", 2)
        } coAnswers {
            UserSearchDetails(5, false, listOf())
        }
        val viewModel = SearchViewModel(SearchUsersUseCase(mockRepo))
        viewModel.onQueryChange("query")
        advanceTimeBy(600)
        val resultSize = viewModel.state.value.userList.asSnapshot().size
        assertEquals(resultSize, 5)
    }
}