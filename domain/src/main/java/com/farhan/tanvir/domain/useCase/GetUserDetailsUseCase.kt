package com.farhan.tanvir.domain.useCase

import com.farhan.tanvir.domain.repository.UserRepository

class GetUserDetailsUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userName: String) = userRepository.getUserDetails(userName = userName)
}
