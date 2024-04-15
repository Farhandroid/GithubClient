package com.farhan.tanvir.feature_user_list.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.farhan.tanvir.common.ResourceState
import com.farhan.tanvir.di.DefaultDispatcher
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.domain.model.UserDetails
import com.farhan.tanvir.domain.useCase.GetUserDetailsUseCase
import com.farhan.tanvir.domain.useCase.GetUserRepositoryUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel
    @AssistedInject
    constructor(
        @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
        @Assisted("userName") private val userName: String,
        private val getUserDetailsUseCase: GetUserDetailsUseCase,
        private val getUserRepositoryUseCase: GetUserRepositoryUseCase,
    ) : ViewModel() {
        private val _onUserRepositoryReceived = Channel<List<Repository>?>()
        val onUserRepositoryReceived: Flow<List<Repository>?> =
            _onUserRepositoryReceived.receiveAsFlow()

        private val _onUserUserDetailsReceived = Channel<UserDetails>()
        val onUserUserDetailsReceived: Flow<UserDetails> =
            _onUserUserDetailsReceived.receiveAsFlow()

        private val _onUserDetailsUiStateChanged = Channel<UserDetailsUiState>()
        val onUserDetailsUiStateChanged: Flow<UserDetailsUiState> =
            _onUserDetailsUiStateChanged.receiveAsFlow()

        init {
            getUserDetails()
            getUserRepository()
        }

        internal fun getUserDetails() =
            viewModelScope.launch(dispatcher) {
                _onUserDetailsUiStateChanged.send(UserDetailsUiState.Loading)
                when (val result = getUserDetailsUseCase.invoke(userName)) {
                    is ResourceState.Error -> {
                        _onUserDetailsUiStateChanged.send(
                            UserDetailsUiState.Error(
                                result.message ?: "Unknown Error",
                            ),
                        )
                    }

                    is ResourceState.Success -> {
                        result.data?.let { _onUserUserDetailsReceived.send(it) }
                    }
                }
            }

        internal fun getUserRepository() =
            viewModelScope.launch(dispatcher) {
                when (val result = getUserRepositoryUseCase.invoke(userName)) {
                    is ResourceState.Error -> {
                        _onUserDetailsUiStateChanged.send(
                            UserDetailsUiState.Error(
                                result.message ?: "Unknown Error",
                            ),
                        )
                    }

                    is ResourceState.Success -> {
                        _onUserRepositoryReceived.send(result.data)
                        _onUserDetailsUiStateChanged.send(
                            UserDetailsUiState.Success,
                        )
                    }
                }
            }

        @dagger.assisted.AssistedFactory
        interface AssistedFactory {
            fun create(
                @Assisted("userName") userName: String,
            ): UserDetailsViewModel
        }

        companion object {
            fun provideFactory(
                assistedFactory: AssistedFactory,
                userName: String,
            ): ViewModelProvider.Factory =
                object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return assistedFactory.create(userName) as T
                    }
                }
        }
    }

sealed class UserDetailsUiState {
    data object Loading : UserDetailsUiState()

    data object Success : UserDetailsUiState()

    data class Error(val errorMessage: String) : UserDetailsUiState()
}
