package com.composeplayground.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RepoSearchResponse(
    @Json(name = "total_count")
    val totalCount: Int,

    @Json(name = "incomplete_results")
    val incompleteResults: Boolean,

    @Json(name = "items")
    val repos: List<RepoResponse>
)

@JsonClass(generateAdapter = true)
data class RepoResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "html_url") val htmlUrl: String,
    @Json(name = "description") val description: String?,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    @Json(name = "language") val language: String?,
)