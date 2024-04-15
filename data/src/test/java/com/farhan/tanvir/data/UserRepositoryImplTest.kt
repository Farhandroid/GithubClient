package com.farhan.tanvir.data

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.data.repository.UserRepositoryImpl
import com.farhan.tanvir.data.util.FakeGithubApiService
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.model.UserDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {
    private lateinit var repository: UserRepositoryImpl
    private val fakeGithubApiService = FakeGithubApiService()

    @Before
    fun setup() {
        repository = UserRepositoryImpl(fakeGithubApiService)
    }

    @Test
    fun `getUsers returns success when API succeeds`() =
        runBlocking {
            val dummyUsers =
                listOf(
                    User(1, "user1", "nodeId1", "avatarUrl1"),
                    User(2, "user2", "nodeId2", "avatarUrl2"),
                )
            fakeGithubApiService.simulateUsersResponseSuccess(dummyUsers)

            val result = repository.getUsers()

            assertTrue(result is ResourceState.Success)
            (result as ResourceState.Success).data?.let { assertEquals(2, it.size) }
            assertEquals("user1", result.data?.get(0)?.login)
        }

    @Test
    fun `getUsers returns error when API fails`() =
        runBlocking {
            fakeGithubApiService.simulateUsersResponseError(404, "Not Found")

            val result = repository.getUsers()

            assertTrue(result is ResourceState.Error)
            assertEquals("Not Found", (result as ResourceState.Error).message)
        }

    @Test
    fun `getUserDetails returns success when API succeeds`() =
        runBlocking {
            val userDetails = UserDetails("user1", 1, "nodeId1", "avatarUrl1")
            fakeGithubApiService.simulateUserDetailsResponseSuccess(userDetails)

            val result = repository.getUserDetails("user1")

            assertTrue(result is ResourceState.Success)
            assertEquals("user1", (result as ResourceState.Success).data?.login)
        }

    @Test
    fun `getUserDetails returns error when API fails`() =
        runBlocking {
            fakeGithubApiService.simulateUserDetailsResponseError(404, "User not found")

            val result = repository.getUserDetails("user1")

            assertTrue(result is ResourceState.Error)
            assertEquals("User not found", (result as ResourceState.Error).message)
        }

    @Test
    fun `getUsersRepo filters non-forked repos and returns success when API succeeds`() =
        runBlocking {
            val repos =
                listOf(
                    Repository(1, "node1", "Repo1", "User/Repo1", fork = false),
                    Repository(2, "node2", "Repo2", "User/Repo2", fork = true), // This is a forked repo
                )
            fakeGithubApiService.simulateUserReposResponseSuccess(repos)

            val result = repository.getUsersRepo("user1")

            assertTrue(result is ResourceState.Success)
            (result as ResourceState.Success).data?.let { assertEquals(1, it.size) }
            assertEquals("Repo1", result.data?.get(0)?.name)
        }

    @Test
    fun `getUsersRepo returns error when API fails`() =
        runBlocking {
            fakeGithubApiService.simulateUserReposResponseError(404, "Repos not found")

            val result = repository.getUsersRepo("user1")

            assertTrue(result is ResourceState.Error)
            assertEquals("Repos not found", (result as ResourceState.Error).message)
        }
}
