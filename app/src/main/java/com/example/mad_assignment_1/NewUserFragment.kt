package com.example.mad_assignment_1

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent

import com.example.mad_assignment_1.databinding.FragmentNewUserBinding


class NewUserFragment: Fragment() {
    private lateinit var binding: FragmentNewUserBinding
    private lateinit var userName: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewUserBinding.inflate(inflater, container, false)
        binding.addButton.setOnClickListener{
            val userName = binding.editTextText.text.toString()
            val resultIntent = Intent().apply{
                putExtra("USER_NAME", userName)
            }
            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
            requireActivity().finish()
        }
        return binding.root

    }

}