package com.farhan.tanvir.feature_user_list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.UserDetails
import com.farhan.tanvir.domain.useCase.GetUserDetailsUseCase
import com.farhan.tanvir.domain.useCase.GetUserRepositoryUseCase
import com.farhan.tanvir.feature_user_list.details.UserDetailsUiState
import com.farhan.tanvir.feature_user_list.details.UserDetailsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var getUserDetailsUseCase: GetUserDetailsUseCase
    private lateinit var getUserRepositoryUseCase: GetUserRepositoryUseCase
    private val userName = "testUser"

    private lateinit var assistedFactory: UserDetailsViewModel.AssistedFactory

    @Before
    fun setUp() {
        getUserDetailsUseCase = mockk(relaxed = true)
        getUserRepositoryUseCase = mockk(relaxed = true)
        assistedFactory =
            mockk {
                every { create(userName) } returns
                    UserDetailsViewModel(
                        dispatcher = testDispatcher,
                        userName = userName,
                        getUserDetailsUseCase = getUserDetailsUseCase,
                        getUserRepositoryUseCase = getUserRepositoryUseCase,
                    )
            }

        val factory =
            UserDetailsViewModel.provideFactory(
                assistedFactory,
                userName,
            )
        viewModel = ViewModelProvider(ViewModelStore(), factory)[UserDetailsViewModel::class.java]
    }

    @Test
    fun `getUserDetails emits loading and success states correctly`() =
        runTest(testDispatcher) {
            val uiStates = mutableListOf<UserDetailsUiState>()
            val userDetailsUpdates = mutableListOf<UserDetails>()

            val uiStateJob =
                launch {
                    viewModel.onUserDetailsUiStateChanged.toList(uiStates)
                }

            val userDetailsJob =
                launch {
                    viewModel.onUserUserDetailsReceived.toList(userDetailsUpdates)
                }

            coEvery { getUserDetailsUseCase.invoke(userName) } returns
                ResourceState.Success(
                    UserDetails(id = 1, name = "John Doe"),
                )

            viewModel.getUserDetails()

            assertTrue(UserDetailsUiState.Loading in uiStates)
            assertTrue(userDetailsUpdates.any { it.name == "John Doe" })

            userDetailsJob.cancel()
            uiStateJob.cancel()
        }

    @Test
    fun `getUserDetails emits error state correctly`() =
        runTest(testDispatcher) {
            coEvery { getUserDetailsUseCase.invoke(userName) } returns ResourceState.Error("API Error")

            val uiStates = mutableListOf<UserDetailsUiState>()

            val uiStateJob =
                launch {
                    viewModel.onUserDetailsUiStateChanged.toList(uiStates)
                }

            viewModel.getUserDetails()

            assertTrue(UserDetailsUiState.Loading in uiStates)
            assertTrue(uiStates.any { it is UserDetailsUiState.Error && it.errorMessage == "API Error" })

            uiStateJob.cancel()
        }

    @Test
    fun `getUserRepository emits success state correctly`() =
        runTest(testDispatcher) {
            val repositoryList = listOf(Repository(id = 1), Repository(id = 2))
            coEvery { getUserRepositoryUseCase.invoke(userName) } returns
                ResourceState.Success(
                    repositoryList,
                )

            val repositoriesUpdates = mutableListOf<List<Repository>?>()
            val uiStates = mutableListOf<UserDetailsUiState>()

            val repositoriesJob =
                launch {
                    viewModel.onUserRepositoryReceived.toList(repositoriesUpdates)
                }
            val uiStateJob =
                launch {
                    viewModel.onUserDetailsUiStateChanged.toList(uiStates)
                }

            viewModel.getUserRepository()

            assertTrue(UserDetailsUiState.Loading in uiStates)
            assertTrue(UserDetailsUiState.Success in uiStates)
            assertTrue(repositoriesUpdates.any { it == repositoryList })

            repositoriesJob.cancel()
            uiStateJob.cancel()
        }

    @Test
    fun `getUserRepository emits error state correctly`() =
        runTest(testDispatcher) {
            coEvery { getUserRepositoryUseCase.invoke(userName) } returns ResourceState.Error("Network Error")

            val uiStates = mutableListOf<UserDetailsUiState>()

            val uiStateJob =
                launch {
                    viewModel.onUserDetailsUiStateChanged.toList(uiStates)
                }

            viewModel.getUserRepository()

            assertTrue(UserDetailsUiState.Loading in uiStates)
            assertTrue(uiStates.any { it is UserDetailsUiState.Error && it.errorMessage == "Network Error" })

            uiStateJob.cancel()
        }
}
