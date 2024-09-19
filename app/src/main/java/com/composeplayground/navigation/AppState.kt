package com.composeplayground.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.composeplayground.feature.details.navigation.navigateToDetailsScreen

class AppState(
    val controller: NavHostController
) {
    fun navigateUp() {
        controller.navigateUp()
    }

    fun navigateToDetailsScreen(
        userId: String,
        name: String?,
        iconUrl: String,
        followingCount: Int,
        followersCount: Int
    ) {
        controller.navigateToDetailsScreen(
            userId = userId,
            name = name,
            iconUrl = iconUrl,
            followingCount = followingCount,
            followersCount = followersCount
        )
    }
}

@Composable
fun rememberAppState(
    controller: NavHostController = rememberNavController()
) = remember(controller) {
    AppState(controller)
}