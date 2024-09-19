package com.composeplayground.feature.search.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.composeplayground.feature.search.SearchScreen

@Serializable
object SearchRoute

fun NavController.navigateToSearchScreen() = navigate(SearchRoute)
fun NavGraphBuilder.addSearchScreen(onUserItemClick: (String, String?, String, Int, Int) -> Unit) {
    composable<SearchRoute> {
        SearchScreen(onUserItemClick)
    }
}