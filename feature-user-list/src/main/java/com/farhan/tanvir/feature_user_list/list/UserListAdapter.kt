package com.farhan.tanvir.feature_user_list.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.farhan.tanvir.domain.model.User
import com.farhan.tanvir.feature_user_list.R
import com.farhan.tanvir.feature_user_list.databinding.LayoutItemUserListBinding

class UserListAdapter(private val onClick: (User?) -> Unit) :
    ListAdapter<User, UserListAdapter.UserListViewHolder>(
        object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserListViewHolder {
        val binding =
            LayoutItemUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserListViewHolder,
        position: Int,
    ) {
        holder.bind(
            item = getItem(position),
        )
    }

    inner class UserListViewHolder(
        private val binding: LayoutItemUserListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User?) {
            binding.apply {
                userNameTV.text = item?.login
                root.setOnClickListener { onClick(item) }

                profileIV.load(item?.avatarUrl) {
                    crossfade(true)
                    placeholder(R.drawable.baseline_account_circle_24)
                    error(R.drawable.baseline_account_circle_24)
                    fallback(R.drawable.baseline_account_circle_24)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}
