package com.farhan.tanvir.domain.util

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails
import com.farhan.tanvir.domain.repository.UserRepository

class FakeUserRepository : UserRepository {
    private var usersResponse: ResourceState<List<User>> =
        ResourceState.Success(
            listOf(
                User(id = 1, login = "Alice"),
                User(id = 2, login = "Bob"),
            ),
        )

    private var userDetailsResponse: ResourceState<UserDetails> =
        ResourceState.Success(
            UserDetails(
                id = 1,
                name = "Alice",
            ),
        )

    private var usersRepoResponse: ResourceState<List<Repository>> =
        ResourceState.Success(
            listOf(
                Repository(
                    id = 1,
                    nodeId = "node1",
                    name = "Repo1",
                    fullName = "User/Repo1",
                    fork = false,
                ),
                Repository(
                    id = 2,
                    nodeId = "node2",
                    name = "Repo2",
                    fullName = "User/Repo2",
                    fork = true,
                ),
            ),
        )

    override suspend fun getUsers(): ResourceState<List<User>> {
        return usersResponse
    }

    override suspend fun getUserDetails(userName: String): ResourceState<UserDetails> {
        return userDetailsResponse
    }

    override suspend fun getUsersRepo(userName: String): ResourceState<List<Repository>> {
        return usersRepoResponse
    }

    fun setUsersResponse(response: ResourceState<List<User>>) {
        usersResponse = response
    }

    fun setUserDetailsResponse(response: ResourceState<UserDetails>) {
        userDetailsResponse = response
    }

    fun setUsersRepoResponse(response: ResourceState<List<Repository>>) {
        usersRepoResponse = response
    }
}
