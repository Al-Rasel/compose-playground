package com.composeplayground.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.composeplayground.domain.usecase.UserReposUseCase
import com.composeplayground.feature.details.navigation.DetailsRoute
import com.composeplayground.ui.extensions.PagingItemLoadStates
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailsViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var pagingItemLoadStates: PagingItemLoadStates

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val stateHandle = SavedStateHandle(
            route = DetailsRoute("userid", "Test User", "test_url", 100, 50),
        )
        viewModel = DetailsViewModel(mockk<UserReposUseCase>(), stateHandle)
    }

    @Test
    fun `initial state should have userId populated`() = runTest {
        assertEquals("userid", viewModel.state.value.userName)
        assertEquals("Test User", viewModel.state.value.userFullName)
        assertEquals("test_url", viewModel.state.value.iconUrl)
        assertEquals(100, viewModel.state.value.followersCount)
        assertEquals(50, viewModel.state.value.followingCount)
    }

    @Test
    fun `loading should be true when data is being fetched`() = runTest {
        every { pagingItemLoadStates.isError() } returns false
        every { pagingItemLoadStates.isEndOfPaginationReached() } returns false
        every { pagingItemLoadStates.isLoading() } returns true
        viewModel.handleLoadState(pagingItemLoadStates)
        assertEquals(true, viewModel.state.value.isLoading)
    }
}
