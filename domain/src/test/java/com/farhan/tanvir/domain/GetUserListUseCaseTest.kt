package com.farhan.tanvir.domain

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.useCase.GetUserListUseCase
import com.farhan.tanvir.domain.util.FakeUserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserListUseCaseTest {
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var getUserListUseCase: GetUserListUseCase

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        getUserListUseCase = GetUserListUseCase(fakeUserRepository)
    }

    @Test
    fun `use case retrieves expected success response from repository`() =
        runBlocking {
            val expectedUsers =
                listOf(
                    User(id = 1, login = "Alice"),
                    User(id = 2, login = "Bob"),
                )
            fakeUserRepository.setUsersResponse(ResourceState.Success(expectedUsers))

            val result = getUserListUseCase()

            assertTrue(result is ResourceState.Success)
            assertEquals(expectedUsers, (result as ResourceState.Success).data)
        }

    @Test
    fun `use case retrieves expected error response from repository`() =
        runBlocking {
            val errorMessage = "Error fetching user list"
            fakeUserRepository.setUsersResponse(ResourceState.Error(errorMessage))

            val result = getUserListUseCase()

            assertTrue(result is ResourceState.Error)
            assertEquals(errorMessage, (result as ResourceState.Error).message)
        }
}
