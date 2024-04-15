package com.farhan.tanvir.di

import com.farhan.tanvir.data.api.GithubApiService
import com.farhan.tanvir.data.repository.UserRepositoryImpl
import com.farhan.tanvir.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideUserRepository(githubApiService: GithubApiService): UserRepository =
        UserRepositoryImpl(
            githubApiService = githubApiService,
        )
}
