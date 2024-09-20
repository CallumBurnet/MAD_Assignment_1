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

/**
 * Fragment for creating a new user profile.
 */
class NewUserFragment : Fragment() {
    private lateinit var binding: FragmentNewUserBinding // View binding for the fragment
    private lateinit var menuInformationModel: MenuInformationModel // ViewModel for managing user data
    private var selectedAvatarResId: Int = R.drawable.avatar // Default avatar resource ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize ViewModel and binding
        menuInformationModel = ViewModelProvider(requireActivity()).get(MenuInformationModel::class.java)
        binding = FragmentNewUserBinding.inflate(inflater, container, false)

        // Set up RecyclerView for avatar selection
        binding.avatarRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.avatarRecyclerView.adapter = AvatarAdapter(menuInformationModel.imageResourceMap) { avatarResId ->
            selectedAvatarResId = avatarResId // Update the selected avatar
            binding.avatarPreview.setImageResource(menuInformationModel.getImageResource(selectedAvatarResId)) // Preview the selected avatar
        }

        // Set up click listener for the "Add" button
        binding.addButton.setOnClickListener {
            val userName = binding.editTextText.text.toString() // Get the user name from input

            // Create a new UserEntity object
            val newUser = UserEntity(
                userID = 0, // The ID will be auto-generated by Room
                name = userName,
                profilePic = selectedAvatarResId // Use the selected avatar resource ID
            )
            menuInformationModel.addUser(newUser) // Add the new user to the ViewModel

            // Navigate back to UserViewFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, UserViewFragment())
                .commit()
        }

        // Set up click listener for the "Return" button
        binding.returnButton?.setOnClickListener {
            // Navigate back to UserViewFragment without adding a new user
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, UserViewFragment())
                .commit()
        }

        return binding.root // Return the root view of the fragment
    }
}
