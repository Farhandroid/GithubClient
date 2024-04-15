package com.farhan.tanvir.di

import com.farhan.tanvir.domain.repository.UserRepository
import com.farhan.tanvir.domain.useCase.GetUserDetailsUseCase
import com.farhan.tanvir.domain.useCase.GetUserListUseCase
import com.farhan.tanvir.domain.useCase.GetUserRepositoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetUserListUseCase(userRepository: UserRepository): GetUserListUseCase = GetUserListUseCase(userRepository = userRepository)

    @Provides
    @Singleton
    fun provideGetUserDetailsUseCase(userRepository: UserRepository): GetUserDetailsUseCase =
        GetUserDetailsUseCase(userRepository = userRepository)

    @Provides
    @Singleton
    fun provideGetUserRepoUseCase(userRepository: UserRepository): GetUserRepositoryUseCase =
        GetUserRepositoryUseCase(userRepository = userRepository)
}
