package com.composeplayground.feature.details

import androidx.compose.runtime.Stable

@Stable
data class DetailsScreenState(
    val isLoading: Boolean = true,
    val userName: String,
    val userFullName: String?,
    val iconUrl: String,
    val followersCount: Int,
    val followingCount: Int
)