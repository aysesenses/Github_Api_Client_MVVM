package com.aysesenses.n11_casestudy.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.n11_casestudy.databinding.ItemUserBinding
import com.aysesenses.n11_casestudy.ui.viewmodel.UserListViewModel

class UserListAdapter(
    private val onClickListener: OnClickListener,
    private val viewModel: UserListViewModel
    ) : ListAdapter<UserEntity, UserListAdapter.UserViewHolder>(DiffCallback()) {

    class UserViewHolder(private var binding: ItemUserBinding, private val viewModel: UserListViewModel) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.user = user
            binding.viewModel = viewModel
            // It forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context)),viewModel)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(user)
        }
        holder.bind(user)
    }

    class DiffCallback : DiffUtil.ItemCallback<UserEntity>() {

        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            Log.e("diff","1"+oldItem.favorite+" yeni "+newItem.favorite)
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            Log.e("diff","2"+oldItem.login+" yeni "+newItem.login)
            return oldItem.favorite == newItem.favorite
        }
    }

        class OnClickListener(val clickListener: (user: UserEntity) -> Unit) {
            fun onClick(user: UserEntity) = clickListener(user)
        }
}
