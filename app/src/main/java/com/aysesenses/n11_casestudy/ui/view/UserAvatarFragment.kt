package com.aysesenses.n11_casestudy.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.aysesenses.n11_casestudy.databinding.FragmentUserAvatarBinding
import com.aysesenses.n11_casestudy.ui.viewmodel.UserAvatarViewModel

class UserAvatarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserAvatarBinding.inflate(inflater)

        val args: UserAvatarFragmentArgs by navArgs()
        val viewModel = ViewModelProvider(this).get(UserAvatarViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel = viewModel

        viewModel.userAvatarUrl.value = args.userAvatarUrl
        return binding.root
    }

}