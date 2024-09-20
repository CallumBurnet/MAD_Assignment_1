package com.example.mad_assignment_1

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider


import com.example.mad_assignment_1.databinding.FragmentNewUserBinding

import androidx.recyclerview.widget.LinearLayoutManager


class NewUserFragment: Fragment() {
    private lateinit var binding: FragmentNewUserBinding
    private lateinit var menuInformationModel: MenuInformationModel
    private var selectedAvatarResId: Int = R.drawable.avatar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewUserBinding.inflate(inflater, container, false)
        binding.avatarRecyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.avatarRecyclerView.adapter = AvatarAdapter(getAvatarList()) { avatarResId ->
            selectedAvatarResId = avatarResId // Update selected avatar
        }

        menuInformationModel = ViewModelProvider(requireActivity()).get(MenuInformationModel::class.java)
        binding.addButton.setOnClickListener{
            val userName = binding.editTextText.text.toString()

            menuInformationModel.addUser(UserProfile(userName, selectedAvatarResId))
           parentFragmentManager.beginTransaction()
               .replace(R.id.mainMenuContainer, UserViewFragment())
               .commit()
        }
        return binding.root


    }
    private fun getAvatarList(): List<Int> {
        // Return a list of drawable resource IDs for avatars
        return listOf(
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,

            // Add more avatars as needed
        )
    }

}