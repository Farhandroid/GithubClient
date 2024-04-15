package com.farhan.tanvir.feature_user_list

import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.useCase.GetUserListUseCase
import com.farhan.tanvir.feature_user_list.list.UserListUiState
import com.farhan.tanvir.feature_user_list.list.UserListViewModel
import io.mockk.coEvery
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
class UserListViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: UserListViewModel
    private lateinit var getUserListUseCase: GetUserListUseCase

    @Before
    fun setUp() {
        getUserListUseCase = mockk(relaxed = true)
        viewModel = UserListViewModel(testDispatcher, getUserListUseCase)
    }

    @Test
    fun `getUserList emits loading and success states correctly`() =
        runTest(testDispatcher) {
            val uiStates = mutableListOf<UserListUiState>()
            val userListUpdates = mutableListOf<List<User>?>()

            val uiStateJob =
                launch(testDispatcher) {
                    viewModel.onUserListUiStateChanged.toList(uiStates)
                }

            val userListJob =
                launch(testDispatcher) {
                    viewModel.userListDataFlow.toList(userListUpdates)
                }

            coEvery { getUserListUseCase.invoke() } returns ResourceState.Success(listOf(User(1, "Test User")))

            viewModel.getUserList()

            assertTrue(uiStates.contains(UserListUiState.Loading))
            assertTrue(uiStates.contains(UserListUiState.Success))
            assertTrue(userListUpdates.any { it?.isNotEmpty() == true })

            uiStateJob.cancel()
            userListJob.cancel()
        }

    @Test
    fun `getUserList handles errors correctly`() =
        runTest(testDispatcher) {
            val uiStates = mutableListOf<UserListUiState>()

            val uiStateJob =
                launch(testDispatcher) {
                    viewModel.onUserListUiStateChanged.toList(uiStates)
                }

            coEvery { getUserListUseCase.invoke() } returns ResourceState.Error("Network Error")

            viewModel.getUserList()

            assertTrue(uiStates.contains(UserListUiState.Loading))
            assertTrue(uiStates.any { it is UserListUiState.Error && it.errorMessage == "Network Error" })

            uiStateJob.cancel()
        }
}
