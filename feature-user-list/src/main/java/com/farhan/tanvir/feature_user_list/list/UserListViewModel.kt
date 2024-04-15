package com.farhan.tanvir.feature_user_list.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.di.DefaultDispatcher
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.domain.useCase.GetUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel
    @Inject
    constructor(
        @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
        private val getUserListUseCase: GetUserListUseCase,
    ) : ViewModel() {
        private val _userListDataFlow: MutableStateFlow<List<User>?> =
            MutableStateFlow(emptyList())
        val userListDataFlow: StateFlow<List<User>?> get() = _userListDataFlow

        private val _onUserListUiStateChanged = Channel<UserListUiState>()
        val onUserListUiStateChanged: Flow<UserListUiState> =
            _onUserListUiStateChanged.receiveAsFlow()

        init {
            getUserList()
        }

        private fun getUserList() =
            viewModelScope.launch(dispatcher) {
                _onUserListUiStateChanged.send(UserListUiState.Loading)
                when (val result = getUserListUseCase.invoke()) {
                    is ResourceState.Error -> {
                        Log.d("apiLog", "getUserList error")
                        _onUserListUiStateChanged.send(UserListUiState.Error(result.message ?: "Unknown Error"))
                    }

                    is ResourceState.Success -> {
                        _userListDataFlow.value = result.data
                        _onUserListUiStateChanged.send(UserListUiState.Success)
                    }
                }
            }
    }

sealed class UserListUiState {
    data object Loading : UserListUiState()

    data object Success : UserListUiState()

    data class Error(val errorMessage: String) : UserListUiState()
}
