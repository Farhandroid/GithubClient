package com.farhan.tanvir.domain.repository

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails

interface UserRepository {
    suspend fun getUsers(): ResourceState<List<User>>

    suspend fun getUserDetails(userName: String): ResourceState<UserDetails>

    suspend fun getUsersRepo(userName: String): ResourceState<List<Repository>>
}
