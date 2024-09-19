package com.composeplayground.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.composeplayground.feature.search.navigation.SearchRoute
import com.composeplayground.feature.search.navigation.addSearchScreen
import com.composeplayground.feature.details.navigation.addDetailsScreen

@Composable
fun AppNavigation(appState: AppState) {
    NavHost(
        navController = appState.controller,
        startDestination = SearchRoute
    ) {
        addSearchScreen(onUserItemClick = appState::navigateToDetailsScreen)
        addDetailsScreen(onBackClick = appState::navigateUp)
    }
}