package com.farhan.tanvir.data.util

import com.farhan.tanvir.data.api.GithubApiService
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeGithubApiService : GithubApiService {
    private var usersResponse: Response<List<User>>? = null
    private var userDetailsResponse: Response<UserDetails>? = null
    private var userReposResponse: Response<List<Repository>>? = null

    override suspend fun getUsers(perPage: Int): Response<List<User>> {
        return usersResponse ?: Response.error(
            404,
            "{\"error\": \"Users Not Found\"}".toResponseBody("application/json".toMediaTypeOrNull()),
        )
    }

    override suspend fun getUserRepos(
        username: String,
        perPage: Int,
    ): Response<List<Repository>> {
        return userReposResponse ?: Response.error(
            404,
            "{\"error\": \"Repositories Not Found\"}".toResponseBody("application/json".toMediaTypeOrNull()),
        )
    }

    override suspend fun getUserDetails(username: String): Response<UserDetails> {
        return userDetailsResponse ?: Response.error(
            404,
            "{\"error\": \"User Details Not Found\"}".toResponseBody("application/json".toMediaTypeOrNull()),
        )
    }

    fun simulateUsersResponseSuccess(users: List<User>) {
        usersResponse = Response.success(users)
    }

    fun simulateUserDetailsResponseSuccess(userDetails: UserDetails) {
        userDetailsResponse = Response.success(userDetails)
    }

    fun simulateUserReposResponseSuccess(repositories: List<Repository>) {
        userReposResponse = Response.success(repositories)
    }

    fun simulateUsersResponseError(
        errorCode: Int,
        errorMessage: String,
    ) {
        usersResponse =
            Response.error(
                errorCode,
                errorMessage.toResponseBody("text/plain".toMediaTypeOrNull()),
            )
    }

    fun simulateUserDetailsResponseError(
        errorCode: Int,
        errorMessage: String,
    ) {
        userDetailsResponse =
            Response.error(
                errorCode,
                errorMessage.toResponseBody("application/json".toMediaTypeOrNull()),
            )
    }

    fun simulateUserReposResponseError(
        errorCode: Int,
        errorMessage: String,
    ) {
        userReposResponse =
            Response.error(
                errorCode,
                errorMessage.toResponseBody("application/json".toMediaTypeOrNull()),
            )
    }
}
