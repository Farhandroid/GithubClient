package com.farhan.tanvir.domain

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.useCase.GetUserRepositoryUseCase
import com.farhan.tanvir.domain.util.FakeUserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserRepositoryUseCaseTest {
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var getUserRepositoryUseCase: GetUserRepositoryUseCase

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        getUserRepositoryUseCase = GetUserRepositoryUseCase(fakeUserRepository)
    }

    @Test
    fun `use case retrieves expected success response from repository`() =
        runBlocking {
            val expectedRepositories =
                listOf(
                    Repository(id = 1, nodeId = "node1", name = "Repo1", fullName = "User/Repo1", fork = false),
                    Repository(id = 2, nodeId = "node2", name = "Repo2", fullName = "User/Repo2", fork = true),
                )
            fakeUserRepository.setUsersRepoResponse(ResourceState.Success(expectedRepositories))

            val result = getUserRepositoryUseCase("user1")

            assertTrue(result is ResourceState.Success)
            assertEquals(expectedRepositories, (result as ResourceState.Success).data)
        }

    @Test
    fun `use case retrieves expected error response from repository`() =
        runBlocking {
            val errorMessage = "Error fetching repositories"
            fakeUserRepository.setUsersRepoResponse(ResourceState.Error(errorMessage))

            val result = getUserRepositoryUseCase("user1")

            assertTrue(result is ResourceState.Error)
            assertEquals(errorMessage, (result as ResourceState.Error).message)
        }
}
