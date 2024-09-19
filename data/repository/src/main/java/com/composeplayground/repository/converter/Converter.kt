package com.composeplayground.repository.converter

import com.composeplayground.model.Repo
import com.composeplayground.model.User
import com.composeplayground.model.UserSearchDetails
import com.composeplayground.network.response.RepoResponse
import com.composeplayground.network.response.UserResponse
import com.composeplayground.network.response.UserSearchResponse

fun UserSearchResponse.toUserSearchDetails(): UserSearchDetails = UserSearchDetails(
    totalCount = totalCount,
    incompleteResults = incompleteResults,
    users = users.map { it.toUser() }
)

fun UserResponse.toUser(): User = User(
    login = login,
    name = name,
    imageUrl = avatarUrl,
    followers = followers ?: 0,
    following = following ?: 0
)

fun RepoResponse.toRepo(): Repo = Repo(
    id = id,
    name = name,
    htmlUrl = htmlUrl,
    description = description,
    stargazersCount = stargazersCount,
    language = language
)