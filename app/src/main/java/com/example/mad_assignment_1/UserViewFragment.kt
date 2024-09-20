package com.example.mad_assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment_1.databinding.FragmentUserViewBinding

class UserViewFragment : Fragment() {
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var adapter: UserProfileAdapter
    private lateinit var menuInformationModel: MenuInformationModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isFirstPlayer = true
        val isInsufficientPlayers = arguments?.getBoolean("INSUFFICIENT_PLAYERS", false) ?: false
        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        menuInformationModel = ViewModelProvider(requireActivity()).get(MenuInformationModel::class.java)

        // Initialize the adapter
        adapter = UserProfileAdapter(emptyList(), { userEntity ->
            Toast.makeText(context, "User ${userEntity.name} clicked", Toast.LENGTH_SHORT).show()
            onUserProfileClick(userEntity)

            if (isFirstPlayer) {
                menuInformationModel.setPrimaryUser(userEntity)
                binding.addSecondUser.visibility = View.VISIBLE
            } else {
                menuInformationModel.setSecondaryUser(userEntity)
            }
        }, menuInformationModel) // Pass the ViewModel here
        binding.recyclerView.adapter = adapter
        if(isInsufficientPlayers){
            Toast.makeText(requireContext(), "Not enough players selected", Toast.LENGTH_LONG).show()
        }

        // Observe user list
        menuInformationModel.getUsers().observe(viewLifecycleOwner) { users ->
            adapter.updateUserProfiles(users)
        }

        // Add user button
        binding.addUser.setOnClickListener {
            val newUserFragment = NewUserFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, newUserFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.addSecondUser.setOnClickListener {
            isFirstPlayer = false
        }

        // Return button
        binding.returnButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, Menu())
                .commit()
        }

        return binding.root
    }

    private fun onUserProfileClick(userEntity: UserEntity) {
        Toast.makeText(requireContext(), "Selected: ${userEntity.name}", Toast.LENGTH_SHORT).show()
    }
}
