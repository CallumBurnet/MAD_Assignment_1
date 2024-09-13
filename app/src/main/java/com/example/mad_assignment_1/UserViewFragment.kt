package com.example.mad_assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment_1.databinding.FragmentUserViewBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class UserViewFragment: Fragment() {
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var adapter: UserProfileAdapter
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        adapter = UserProfileAdapter(userViewModel.getUsers().value?: mutableListOf())
        binding.recyclerView.adapter = adapter


        userViewModel.getUsers().observe(viewLifecycleOwner, { users ->
            adapter.updateUserProfiles(users)
        })

        //Add user button
        binding.addUser.setOnClickListener{
            val newUserFragment = NewUserFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newUserFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root

    }

}