package com.farhan.tanvir.feature_user_list.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.farhan.tanvir.common.launchAndRepeatWithViewLifecycle
import com.farhan.tanvir.feature_user_list.R
import com.farhan.tanvir.feature_user_list.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {
    private val navArgs: UserDetailsFragmentArgs by navArgs()

    private lateinit var repositoryAdapter: RepositoryAdapter

    @Inject
    lateinit var userDetailsViewModelFactory: UserDetailsViewModel.AssistedFactory
    private val userDetailsViewModel: UserDetailsViewModel by viewModels {
        UserDetailsViewModel.provideFactory(
            userDetailsViewModelFactory,
            navArgs.userName,
        )
    }

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUsersRepositoryDataCollector()
        setUserDetailsUiFlowCollector()
        setUsersDetailsDataCollector()
    }

    private fun setupRecyclerView() {
        repositoryAdapter =
            RepositoryAdapter { repository ->

                repository?.name?.let { repositoryName ->
                    Intent(Intent.ACTION_VIEW).apply {
                        data =
                            Uri.parse("https://github.com/${navArgs.userName}/$repositoryName")
                    }.also {
                        startActivity(it)
                    }
                }
            }
        binding.recyclerView.apply {
            adapter = repositoryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUserDetailsUiFlowCollector() =
        launchAndRepeatWithViewLifecycle {
            userDetailsViewModel.onUserDetailsUiStateChanged.collect { state ->
                when (state) {
                    is UserDetailsUiState.Loading -> {
                        binding.apply {
                            progressBar.isVisible = true
                            recyclerView.isVisible = false
                        }
                    }

                    is UserDetailsUiState.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                            recyclerView.isVisible = true
                        }
                    }

                    is UserDetailsUiState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    private fun setUsersRepositoryDataCollector() =
        launchAndRepeatWithViewLifecycle {
            userDetailsViewModel.onUserRepositoryReceived.collect { repository ->
                repositoryAdapter.submitList(repository)
            }
        }

    private fun setUsersDetailsDataCollector() =
        launchAndRepeatWithViewLifecycle {
            userDetailsViewModel.onUserUserDetailsReceived.collect { userDetails ->
                binding.apply {
                    userProfileIV.load(userDetails.avatarUrl) {
                        crossfade(true)
                        placeholder(R.drawable.baseline_account_circle_24)
                        error(R.drawable.baseline_account_circle_24)
                        fallback(R.drawable.baseline_account_circle_24)
                        transformations(CircleCropTransformation())
                    }

                    userNameTV.text = userDetails.login
                    fullNameTV.text = userDetails.name
                    followersCountTV.text = userDetails.followers.toString()
                    followingCountTV.text = userDetails.following.toString()
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
