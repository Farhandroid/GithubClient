package com.farhan.tanvir.data.api

import com.farhan.tanvir.common.Constant
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int = Constant.ITEM_PER_PAGE,
    ): Response<List<User>>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = Constant.ITEM_PER_PAGE,
    ): Response<List<Repository>>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String,
    ): Response<UserDetails>
}
