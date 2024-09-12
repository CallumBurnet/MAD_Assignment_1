package com.example.mad_assignment_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import com.example.mad_assignment_1.databinding.FragmentMenuBinding

class Menu : Fragment() {
    private lateinit var binding: FragmentMenuBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        binding.newGameButton.setOnClickListener { view ->
            val fragment = NewGame()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, fragment)
                .commit()
        }
        binding.profileButton.setOnClickListener{view ->
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}