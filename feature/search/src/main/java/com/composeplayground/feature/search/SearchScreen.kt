package com.composeplayground.feature.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.composeplayground.model.User
import com.composeplayground.ui.action.ShowToastMessage
import com.composeplayground.ui.component.DynamicAsyncImageWithPlaceHolder
import com.composeplayground.ui.extensions.pendingLoadStates
import com.composeplayground.ui.extensions.rememberLazyPagingListState
import com.composeplayground.ui.icons.CoreIcons
import com.composeplayground.ui.uievent.UiEventHandler
import com.composeplayground.ui.uievent.rememberUiEventDispatcher
import com.composeplayground.feature.search.R as searchR

@Composable
fun SearchScreen(
    onUserItemClick: (String, String?, String, Int, Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userList = state.userList.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(viewModel.resultAction) {
        viewModel.resultAction.collect { event ->
            when (event) {
                is ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    LaunchedEffect(userList) {
        snapshotFlow {
            userList.pendingLoadStates()
        }.collect { loadState ->
            viewModel.handleLoadState(loadState)
        }
    }
    UiEventHandler(handle = { uiEvent ->
        when (uiEvent) {
            is SearchUiEvent.OnItemClick -> {
                onUserItemClick(
                    uiEvent.user.login,
                    uiEvent.user.name,
                    uiEvent.user.imageUrl,
                    uiEvent.user.followers,
                    uiEvent.user.following
                )
            }
        }

    }) {
        SearchContent(
            state.searchQuery,
            list = userList,
            isLoading = state.isLoading,
            viewModel::onQueryChange
        )
    }
}

@Composable
private fun SearchContent(
    query: String,
    list: LazyPagingItems<User>,
    isLoading: Boolean,
    onSearchQueryChanged: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchToolbar(
                searchQuery = query,
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = { s -> }

            )
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            UserListContent(list = list)
        }
    }
}

@Composable
private fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = CoreIcons.Search,
                contentDescription = stringResource(
                    id = searchR.string.feature_search_title,
                ),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = CoreIcons.Close,
                        contentDescription = stringResource(
                            id = searchR.string.feature_search_clear_search_text_content_desc,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Composable
private fun UserListContent(list: LazyPagingItems<User>) {
    val listState = list.rememberLazyPagingListState()
    LazyColumn(
        modifier = Modifier.padding(horizontal = 0.dp),
        state = listState,
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(count = list.itemCount,
            key = list.itemKey { it.login }) { index ->
            val user = list[index] ?: return@items

            UserItem(user)
        }
        item {
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
    }
}

@Composable
private fun UserItem(user: User, modifier: Modifier = Modifier) {
    val dispatch = rememberUiEventDispatcher()
    ListItem(
        leadingContent = {
            DynamicAsyncImageWithPlaceHolder(
                imageUrl = user.imageUrl,
                contentDescription = null,
                modifier = modifier.size(64.dp),
            )
        },
        headlineContent = {
            Text(text = user.login)
        },
        modifier = modifier.clickable(enabled = true, onClick = {
            dispatch(SearchUiEvent.OnItemClick(user))
        })
    )
}