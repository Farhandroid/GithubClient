package com.farhan.tanvir.data.repository

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.data.api.GithubApiService
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails
import com.farhan.tanvir.domain.repository.UserRepository

class UserRepositoryImpl(private val githubApiService: GithubApiService) : UserRepository {
    override suspend fun getUsers(): ResourceState<List<User>> {
        return try {
            val response = githubApiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                ResourceState.Success(response.body())
            } else {
                ResourceState.Error(response.message().ifEmpty { "Unknown error" })
            }
        } catch (e: Exception) {
            ResourceState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun getUserDetails(userName: String): ResourceState<UserDetails> {
        return try {
            val response = githubApiService.getUserDetails(userName)
            if (response.isSuccessful && response.body() != null) {
                ResourceState.Success(response.body())
            } else {
                ResourceState.Error(response.message().ifEmpty { "Unknown error" })
            }
        } catch (e: Exception) {
            ResourceState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun getUsersRepo(userName: String): ResourceState<List<Repository>> {
        return try {
            val response = githubApiService.getUserRepos(userName)
            if (response.isSuccessful && response.body() != null) {
                val nonForkedRepositories =
                    response.body()!!.filter { repo ->
                        repo.fork == false || repo.fork == null
                    }
                ResourceState.Success(nonForkedRepositories)
            } else {
                ResourceState.Error(response.message().ifEmpty { "Unknown error" })
            }
        } catch (e: Exception) {
            ResourceState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }
}
