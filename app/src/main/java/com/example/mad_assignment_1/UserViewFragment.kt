package com.example.mad_assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment_1.databinding.FragmentUserViewBinding

class UserViewFragment: Fragment() {
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var adapter: UserProfileAdapter
    private var users = mutableListOf<UserProfile>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserProfileAdapter(users)
        binding.recyclerView.adapter = adapter

        users.add(UserProfile("Callum", R.drawable.ic_launcher_background))
        adapter.updateUserProfiles(users)
        return binding.root

    }
    fun updateUserProfiles(newUserProfiles: List<UserProfile>) {
        adapter.updateUserProfiles(newUserProfiles)
    }
}