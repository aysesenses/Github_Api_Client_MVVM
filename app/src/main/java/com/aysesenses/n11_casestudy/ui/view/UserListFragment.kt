package com.aysesenses.n11_casestudy.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.aysesenses.n11_casestudy.databinding.FragmentUserListBinding
import com.aysesenses.n11_casestudy.ui.adapter.UserListAdapter
import com.aysesenses.n11_casestudy.ui.adapter.bindRecyclerView
import com.aysesenses.n11_casestudy.ui.viewmodel.UserListViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args: UserListFragmentArgs by navArgs()

        val binding = FragmentUserListBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        (this.activity as AppCompatActivity).supportActionBar?.title = "Github Users"

        //This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel = viewModel

        viewModel.userSearch(args.term) //!!!

        binding.userListRecyclerView.adapter = UserListAdapter(UserListAdapter.OnClickListener {
            val action = it.login.let { login ->
                UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(
                    login!!
                )
            }
            findNavController().navigate(action)
        },viewModel)

        // Add observer for users and
        viewModel.users.observe(viewLifecycleOwner) {
            bindRecyclerView(binding.userListRecyclerView, it)
        }
        return binding.root
    }
}