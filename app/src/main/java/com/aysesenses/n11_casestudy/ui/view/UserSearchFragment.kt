package com.aysesenses.n11_casestudy.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.aysesenses.n11_casestudy.databinding.FragmentUserSearchBinding

class UserSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUserSearchBinding.inflate(inflater)
        (this.activity as AppCompatActivity).supportActionBar?.title = "Search User"

        binding.userSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searhTerm: String): Boolean {
                searhTerm.let { term ->
                    if (term.length > 2) {
                        val action =
                            UserSearchFragmentDirections.actionUserSearchFragmentToUserListFragment(
                                term
                            )
                        findNavController().navigate(action)
                    }
                }
                return true
            }

            override fun onQueryTextChange(searchTerm: String?): Boolean {
                return true
            }
        })

        binding.userSearchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.userSearchView.onActionViewCollapsed()
                return true
            }
        })

        binding.userSearchView.setOnClickListener {
            binding.userSearchView.isIconified = false
        }
        return binding.root
    }

}