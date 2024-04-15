package com.farhan.tanvir.domain

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.UserDetails
import com.farhan.tanvir.domain.useCase.GetUserDetailsUseCase
import com.farhan.tanvir.domain.util.FakeUserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserDetailsUseCaseTest {
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var getUserDetailsUseCase: GetUserDetailsUseCase

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        getUserDetailsUseCase = GetUserDetailsUseCase(fakeUserRepository)
    }

    @Test
    fun `use case retrieves expected success response from repository`() =
        runBlocking {
            val expectedUserDetails =
                UserDetails(
                    id = 1,
                    name = "Alice",
                )
            fakeUserRepository.setUserDetailsResponse(ResourceState.Success(expectedUserDetails))

            val result = getUserDetailsUseCase("Alice")

            assertTrue(result is ResourceState.Success)
            assertEquals(expectedUserDetails, (result as ResourceState.Success).data)
        }

    @Test
    fun `use case retrieves expected error response from repository`() =
        runBlocking {
            val errorMessage = "Error fetching user details"
            fakeUserRepository.setUserDetailsResponse(ResourceState.Error(errorMessage))

            val result = getUserDetailsUseCase("Alice")

            assertTrue(result is ResourceState.Error)
            assertEquals(errorMessage, (result as ResourceState.Error).message)
        }
}
