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
        binding = FragmentMenuBinding.inflate(inflater, container, false);

        val primaryUser = menuInformationModel.getPrimaryUser()
        val secondaryUser = menuInformationModel.getSecondaryUser()
        if(primaryUser != null){
            binding.profileOneImage.setImageResource(primaryUser.imageResId)
            binding.userOneText.setText("User 1")
        }
        if(secondaryUser != null){
            binding.profileTwoImage.setImageResource(secondaryUser.imageResId)
            binding.userTwoText.setText("User 2")

        }
        binding.newGameButton.setOnClickListener { view ->
            (activity as MainActivity).loadNewGameFragment()
        }
        binding.profileButton.setOnClickListener{view ->
            (activity as MainActivity).loadUserProfileFragment()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}