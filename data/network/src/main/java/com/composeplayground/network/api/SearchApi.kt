package com.composeplayground.network.api

import com.composeplayground.network.response.RepoSearchResponse
import com.composeplayground.network.response.UserResponse
import com.composeplayground.network.response.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {
    @GET("search/users")
    suspend fun fetchUserList(
        @Query("q") query: String,
        @Query("page") page: Int
    ): UserSearchResponse

    @GET("search/repositories")
    suspend fun fetchRepoList(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): RepoSearchResponse

    // todo: put in new file
    @GET("users/{login}")
    suspend fun fetchUser(
        @Path("login") userId: String
    ): UserResponse

}
