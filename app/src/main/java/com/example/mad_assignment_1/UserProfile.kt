package com.example.mad_assignment_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment_1.databinding.FragmentUserProfileBinding
import com.example.mad_assignment_1.databinding.UserProfileViewBinding

class UserProfile : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        val profiles = MutableList(1) { index ->
            UserProfileData("User ${index + 1}", R.drawable.smily_face)
        }
        val adapter = UserProfileAdapter(profiles)
        binding.userProfileCreationName.hint = "User ${adapter.itemCount + 1}"

        binding.userAddButton.setOnClickListener { view ->
            if (binding.userProfileCreationName.text == null) {
                adapter.add(
                    UserProfileData(
                        binding.userProfileCreationName.hint.toString(),
                        R.drawable.smily_face
                    )
                )
            } else {
                adapter.add(
                    UserProfileData(
                        binding.userProfileCreationName.text.toString(),
                        R.drawable.smily_face
                    )
                )
            }
            binding.userProfileCreationName.hint = "User ${adapter.itemCount + 1}"
            binding.userProfileList.scrollToPosition(adapter.itemCount - 1)
        }
        binding.userProfileList.adapter = adapter
        binding.userProfileList.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

}