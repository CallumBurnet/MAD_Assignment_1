package com.example.mad_assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment_1.databinding.FragmentUserViewBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class UserViewFragment: Fragment() {
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var adapter: UserProfileAdapter
    private lateinit var menuInformationModel: MenuInformationModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isFirstPlayer:  Boolean = true;

        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        menuInformationModel = ViewModelProvider(requireActivity()).get(MenuInformationModel::class.java)

        adapter = UserProfileAdapter(menuInformationModel.getUsers().value ?: emptyList()) { userProfile ->
            // Handle item click
            Toast.makeText(context, "User ${userProfile.userName} clicked", Toast.LENGTH_SHORT).show()

            onUserProfileClick(userProfile)
            if(isFirstPlayer){
                menuInformationModel.setPrimaryUser(userProfile)
                binding.addSecondUser.visibility = View.VISIBLE;
            }else{
                menuInformationModel.setSecondaryUser(userProfile)
            }
        }
        binding.recyclerView.adapter = adapter


        menuInformationModel.getUsers().observe(viewLifecycleOwner, { users ->
            adapter.updateUserProfiles(users)
        })

        //Add user button
        binding.addUser.setOnClickListener{
            val newUserFragment = NewUserFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, newUserFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.addSecondUser.setOnClickListener{
            isFirstPlayer = false;
        }
        //return button
        binding.returnButton.setOnClickListener{
            val fm = requireActivity().supportFragmentManager;
            fm.beginTransaction().replace(R.id.mainMenuContainer, Menu()).commit()
        }

        return binding.root
    }
    fun onUserProfileClick(userProfile: UserProfile){
        Toast.makeText(requireContext(), "UURR", Toast.LENGTH_SHORT).show()
    }


}