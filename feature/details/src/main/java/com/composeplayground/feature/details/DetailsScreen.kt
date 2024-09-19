package com.composeplayground.feature.details

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.composeplayground.model.Repo
import com.composeplayground.ui.action.ShowToastMessage
import com.composeplayground.ui.component.DynamicAsyncImageWithPlaceHolder
import com.composeplayground.ui.extensions.openUrlWithCustomTabs
import com.composeplayground.ui.extensions.pendingLoadStates
import com.composeplayground.ui.extensions.rememberLazyPagingListState
import com.composeplayground.ui.uievent.UiEventHandler
import com.composeplayground.ui.uievent.rememberUiEventDispatcher

@Composable
fun DetailsScreen(viewModel: DetailsViewModel = hiltViewModel(), onBackClick: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val repositoryList = viewModel.repositoryList.collectAsLazyPagingItems()
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
    LaunchedEffect(repositoryList) {
        snapshotFlow { repositoryList.pendingLoadStates() }.collect(viewModel::handleLoadState)
    }
    UiEventHandler(handle = { uiEvent ->
        when (uiEvent) {
            is DetailsUiEvent.OnRepoClick -> {
                context.openUrlWithCustomTabs(
                    url = uiEvent.url,
                    configure = {
                        setShowTitle(false)
                    }
                )
            }
        }

    }) {
        DetailsContent(state, repositoryList = repositoryList, onBackClick)
    }
}

@Composable
fun DetailsContent(
    state: DetailsScreenState,
    repositoryList: LazyPagingItems<Repo>,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            UserItemWithDetails(
                state.userName,
                state.userFullName,
                followersCount = state.followersCount,
                followingCount = state.followingCount,
                state.iconUrl,
                onBackClick,
                modifier = Modifier
            )
            if (state.isLoading) {
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
            RepoListContent(list = repositoryList)
        }
    }
}

@Composable
private fun UserItemWithDetails(
    userId: String,
    fullName: String?,
    followersCount: Int,
    followingCount: Int,
    icon: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "back",
                )
            }
        },
        overlineContent = {
            fullName?.let {
                Text(it)
            }
        },
        headlineContent = {
            Text(userId, fontWeight = FontWeight.Bold)
        },
        supportingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "person",
                    modifier = modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(text = "$followersCount followers • ")
                Text(text = "$followingCount following")
            }
        },
        trailingContent = {
            DynamicAsyncImageWithPlaceHolder(
                imageUrl = icon,
                contentDescription = null,
                modifier = modifier.size(64.dp),
            )
        }
    )
}


@Composable
private fun RepoListContent(list: LazyPagingItems<Repo>) {
    val listState = list.rememberLazyPagingListState()
    LazyColumn(
        modifier = Modifier.padding(horizontal = 0.dp),
        state = listState,
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(count = list.itemCount,
            key = list.itemKey { it.name }) { index ->
            val repo = list[index] ?: return@items

            RepoItem(
                repo.name,
                repo.description,
                language = repo.language,
                repo.stargazersCount,
                repo.htmlUrl
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
    }
}


@Composable
private fun RepoItem(
    name: String,
    details: String?,
    language: String?,
    numberOfStarts: Int,
    url: String,
    modifier: Modifier = Modifier
) {
    val dispatch = rememberUiEventDispatcher()
    ListItem(
        headlineContent = {
            Text(text = name, fontWeight = FontWeight.Bold)
        },
        supportingContent = {
            Column {
                details?.let { Text(text = it) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "star",
                        modifier = modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = "$numberOfStarts  ")
                    language?.let { Text(text = "•  $it") }
                }
            }
        },
        modifier = modifier.clickable(enabled = true, onClick = {
            dispatch(DetailsUiEvent.OnRepoClick(url))
        })
    )
}