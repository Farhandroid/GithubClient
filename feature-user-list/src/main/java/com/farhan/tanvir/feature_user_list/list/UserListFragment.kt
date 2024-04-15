package com.farhan.tanvir.feature_user_list.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhan.tanvir.common.launchAndRepeatWithViewLifecycle
import com.farhan.tanvir.feature_user_list.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {
    private val viewModel: UserListViewModel by viewModels()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUserListDataCollector()
        setUserListUiFlowCollector()
    }

    private fun setupRecyclerView() {
        userListAdapter =
            UserListAdapter { user ->
                findNavController().navigate(
                    directions =
                        UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(
                            userName = user?.login ?: "",
                        ),
                )
            }
        binding.recyclerView.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUserListDataCollector() =
        launchAndRepeatWithViewLifecycle {
            viewModel.userListDataFlow.collect { users ->
                userListAdapter.submitList(users)
            }
        }

    private fun setUserListUiFlowCollector() =
        launchAndRepeatWithViewLifecycle {
            viewModel.onUserListUiStateChanged.collect { state ->
                when (state) {
                    is UserListUiState.Loading -> {
                        binding.apply {
                            progressBar.isVisible = true
                            recyclerView.isVisible = false
                        }
                    }

                    is UserListUiState.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                            recyclerView.isVisible = true
                        }
                    }

                    is UserListUiState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
