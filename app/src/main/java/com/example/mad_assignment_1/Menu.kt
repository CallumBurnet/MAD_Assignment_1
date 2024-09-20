package com.example.mad_assignment_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment_1.databinding.FragmentMenuBinding

class Menu : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private val menuInformationModel: MenuInformationModel by activityViewModels { MenuInformationModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        val primaryUser = menuInformationModel.getPrimaryUser()
        val secondaryUser = menuInformationModel.getSecondaryUser()

        if (primaryUser != null) {
            val primaryImageResId = menuInformationModel.getImageResource(primaryUser.profilePic)
            if (primaryImageResId != null) {
                binding.profileOneImage.setImageResource(primaryImageResId)
            } else {
                binding.profileOneImage.setImageResource(R.drawable.avatar2) // Default avatar if not found
            }
            binding.userOneText.text = primaryUser.name // Assuming you have a name field
        }

        if (secondaryUser != null) {
            val secondaryImageResId = menuInformationModel.getImageResource(secondaryUser.profilePic)
            if (secondaryImageResId != null) {
                binding.profileTwoImage.setImageResource(secondaryImageResId)
            } else {
                binding.profileTwoImage.setImageResource(R.drawable.avatar2) // Default avatar if not found
            }
            binding.userTwoText.text = secondaryUser.name // Assuming you have a name field
        }

        binding.newGameButton.setOnClickListener {
            (activity as MainActivity).loadNewGameFragment()
        }
        binding.profileButton.setOnClickListener {
            (activity as MainActivity).loadUserProfileFragment()
        }

        return binding.root
    }
}
