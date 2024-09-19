package com.composeplayground.repository.di

import com.composeplayground.repository.UserReposRepository
import com.composeplayground.repository.UserSearchRepository
import com.composeplayground.repository.repos.UserReposRepositoryImpl
import com.composeplayground.repository.search.UserSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindsModule {
    @Singleton
    @Binds
    abstract fun bindUserSearchRepository(
        userSearchRepository: UserSearchRepositoryImpl
    ): UserSearchRepository

    @Singleton
    @Binds
    abstract fun bindUserReposRepository(
        userReposRepository: UserReposRepositoryImpl
    ): UserReposRepository
}
