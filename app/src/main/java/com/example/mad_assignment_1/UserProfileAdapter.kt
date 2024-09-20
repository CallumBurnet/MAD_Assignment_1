package com.example.mad_assignment_1
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.UserProfileBinding
import android.view.LayoutInflater
import android.view.ViewGroup


class UserProfileAdapter(
    private var userProfiles: List<UserProfile>, // List of UserProfile data
    private val onItemClick: (UserProfile) -> Unit // List of UserProfile data

) : RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val binding = UserProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(userProfiles[position])

    }

    override fun getItemCount(): Int = userProfiles.size

    fun updateUserProfiles(newUserProfiles: List<UserProfile>) {
        userProfiles = newUserProfiles
        notifyDataSetChanged()
    }


    inner class UserProfileViewHolder(private val binding: UserProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener on item view
            binding.root.setOnClickListener {
                // Call the click listener with the current userProfile
                val userProfile = userProfiles[adapterPosition]
                onItemClick(userProfile)
            }
        }

        fun bind(userProfile: UserProfile) {
            binding.userName.text = userProfile.userName
            binding.avatarView.setImageResource(userProfile.imageResId)
        }
    }
}
