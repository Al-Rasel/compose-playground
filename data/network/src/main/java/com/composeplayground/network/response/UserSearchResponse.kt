package com.composeplayground.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSearchResponse(
    @Json(name = "total_count")
    val totalCount: Int,

    @Json(name = "incomplete_results")
    val incompleteResults: Boolean,

    @Json(name = "items")
    val users: List<UserResponse>
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "login")
    val login: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "followers")
    val followers: Int?,
    @Json(name = "following")
    val following: Int?,
)
