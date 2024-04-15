package com.farhan.tanvir.feature_user_list.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farhan.tanvir.domain.model.Repository
import com.farhan.tanvir.feature_user_list.databinding.LayoutItemUserRepositoryBinding

class RepositoryAdapter(private val onClick: (Repository?) -> Unit) :
    ListAdapter<Repository, RepositoryAdapter.RepositoryViewHolder>(
        object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(
                oldItem: Repository,
                newItem: Repository,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Repository,
                newItem: Repository,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RepositoryViewHolder {
        val binding =
            LayoutItemUserRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RepositoryViewHolder,
        position: Int,
    ) {
        holder.bind(
            item = getItem(position),
        )
    }

    inner class RepositoryViewHolder(
        private val binding: LayoutItemUserRepositoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repository?) {
            binding.apply {
                // Set text or hide the view if the value is null
                repositoryNameTV.apply {
                    text = item?.name
                    visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
                repositoryLanguageTV.apply {
                    text = item?.language
                    visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
                repositoryDescTV.apply {
                    text = item?.description
                    visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
                repositoryStartTV.apply {
                    text = item?.stargazersCount?.toString()
                        ?: "0"
                    visibility = if (text == "0") View.GONE else View.VISIBLE
                }

                root.setOnClickListener { onClick(item) }
            }
        }
    }
}
