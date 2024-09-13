package com.example.mad_assignment_1

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import androidx.lifecycle.ViewModelProvider


import com.example.mad_assignment_1.databinding.FragmentNewUserBinding


class NewUserFragment: Fragment() {
    private lateinit var binding: FragmentNewUserBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewUserBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.addButton.setOnClickListener{
            val userName = binding.editTextText.text.toString()
            userViewModel.addUser(UserProfile(userName, R.drawable.ic_launcher_background))
           parentFragmentManager.beginTransaction()
               .replace(R.id.fragmentContainer, UserViewFragment())
               .commit()
        }
        return binding.root

    }

}