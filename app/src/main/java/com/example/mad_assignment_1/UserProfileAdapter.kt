package com.example.mad_assignment_1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.UserProfileViewBinding

class UserProfileAdapter(private val profiles: MutableList<UserProfileData>) : RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        Log.d("UserProfile", "Added user")
        val binding = UserProfileViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    fun add(profile: UserProfileData) {
        this.profiles.add(profile)
        notifyItemInserted(itemCount - 1)
    }
    class UserProfileViewHolder(private val binding: UserProfileViewBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(profile: UserProfileData) {
                    binding.userProfileName.text = profile.name
                    binding.userProfileImage.setImageResource(profile.image)
                }
            }
}