package com.example.mad_assignment_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mad_assignment_1.databinding.FragmentMenuBinding

class Menu : Fragment() {
    // Binding to access views in the fragment
    private lateinit var binding: FragmentMenuBinding
    // Using activityViewModels to share the MenuInformationModel with the activity
    private val menuInformationModel: MenuInformationModel by activityViewModels { MenuInformationModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        // Get the primary and secondary users from the model
        val primaryUser = menuInformationModel.getPrimaryUser()
        val secondaryUser = menuInformationModel.getSecondaryUser()

        // Set up the primary user's profile image and name
        if (primaryUser != null) {
            val primaryImageResId = menuInformationModel.getImageResource(primaryUser.profilePic)
            // Set the image resource, defaulting to a placeholder if not found
            if (primaryImageResId != null) {
                binding.profileOneImage.setImageResource(primaryImageResId)
            } else {
                binding.profileOneImage.setImageResource(R.drawable.avatar2) // Default avatar if not found
            }
            // Display the user's name
            binding.userOneText.text = primaryUser.name // Assuming you have a name field
        }

        // Set up the secondary user's profile image and name
        if (secondaryUser != null) {
            val secondaryImageResId = menuInformationModel.getImageResource(secondaryUser.profilePic)
            // Set the image resource, defaulting to a placeholder if not found
            if (secondaryImageResId != null) {
                binding.profileTwoImage.setImageResource(secondaryImageResId)
            } else {
                binding.profileTwoImage.setImageResource(R.drawable.avatar2) // Default avatar if not found
            }
            // Display the user's name
            binding.userTwoText.text = secondaryUser.name // Assuming you have a name field
        }

        // Set up click listener for the "New Game" button
        binding.newGameButton.setOnClickListener {
            (activity as MainActivity).loadNewGameFragment() // Navigate to the new game fragment
        }

        // Set up click listener for the "Profile" button
        binding.profileButton.setOnClickListener {
            (activity as MainActivity).loadUserProfileFragment() // Navigate to the user profile fragment
        }

        // Return the root view of the binding
        return binding.root
    }
}
