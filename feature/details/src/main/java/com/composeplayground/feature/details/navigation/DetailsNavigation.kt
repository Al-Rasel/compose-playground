package com.composeplayground.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.composeplayground.feature.details.DetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRoute(
    val userId: String,
    val userFullName: String?,
    val iconUrl: String,
    val followersCount: Int,
    val followingCount: Int
)

fun NavController.navigateToDetailsScreen(
    userId: String,
    name: String?,
    iconUrl: String,
    followersCount: Int,
    followingCount: Int
) = navigate(
    DetailsRoute(
        userId = userId,
        userFullName = name,
        iconUrl = iconUrl,
        followersCount = followersCount,
        followingCount = followingCount
    )
)

fun NavGraphBuilder.addDetailsScreen(onBackClick: () -> Unit) {
    composable<DetailsRoute> {
        DetailsScreen(onBackClick = onBackClick)
    }
}
