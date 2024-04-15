package com.farhan.tanvir.domain.useCase

import com.farhan.tanvir.domain.repository.UserRepository

class GetUserRepositoryUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userName: String) = userRepository.getUsersRepo(userName = userName)
}
