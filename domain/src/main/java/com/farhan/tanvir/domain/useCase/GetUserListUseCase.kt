package com.farhan.tanvir.domain.useCase

import com.farhan.tanvir.domain.repository.UserRepository

class GetUserListUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getUsers()
}
