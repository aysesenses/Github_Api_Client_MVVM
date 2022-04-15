package com.aysesenses.n11_casestudy.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aysesenses.n11_casestudy.databinding.FragmentUserDetailBinding
import com.aysesenses.n11_casestudy.ui.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: UserDetailFragmentArgs by navArgs()

        val binding = FragmentUserDetailBinding.inflate(inflater)

        val viewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)

        //This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Giving the binding access to the UserDetailViewModel
        binding.viewModel = viewModel

        // Add observer for score
        viewModel.userDetail.observe(viewLifecycleOwner){
            (this.activity as AppCompatActivity).supportActionBar?.title = it.name
            binding.userDetail = it
        }

        viewModel.getUserDetails(args.userlogin)

        binding.userAddFavoriteButton.setOnClickListener {
         //   viewModel.favorite(binding.userLoginTextView.text as String,binding.userAddFavoriteButton)
        }

        binding.userAvatarImageView.setOnClickListener {
            val userAvatarUrl = viewModel.userDetail.value?.avatar_url
            val action = userAvatarUrl?.let { url ->
                UserDetailFragmentDirections.actionUserDetailFragmentToUserAvatarFragment(url)
            }
            findNavController().navigate(action!!)
        }

        return binding.root
    }

}