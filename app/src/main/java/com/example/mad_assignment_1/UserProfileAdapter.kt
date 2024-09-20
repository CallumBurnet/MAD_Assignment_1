package com.example.mad_assignment_1

import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.UserProfileBinding
import android.view.LayoutInflater
import android.view.ViewGroup

class UserProfileAdapter(
    private var userEntities: List<UserEntity>, // List of UserEntity data
    private val onItemClick: (UserEntity) -> Unit, // Click listener for UserEntity
    private val menuInformationModel: MenuInformationModel // Pass the ViewModel
) : RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val binding = UserProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(userEntities[position])
    }

    override fun getItemCount(): Int = userEntities.size

    fun updateUserProfiles(newUserEntities: List<UserEntity>) {
        userEntities = newUserEntities
        notifyDataSetChanged()
    }

    inner class UserProfileViewHolder(private val binding: UserProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener on item view
            binding.root.setOnClickListener {
                // Call the click listener with the current userEntity
                val userEntity = userEntities[adapterPosition]
                onItemClick(userEntity)
            }
        }

        fun bind(userEntity: UserEntity) {
            binding.userName.text = userEntity.name
            binding.userWins.text = "Wins :${userEntity.wins}"
            binding.userLosses.text = "Losses : ${userEntity.losses}"
            var gamesPlayed = userEntity.wins + userEntity.losses;
            binding.gamesPlayed.text = "Games Played : ${gamesPlayed}"
            val winPercentage = if (userEntity.losses + userEntity.wins > 0) {
                val percentage = (userEntity.wins.toDouble() / (userEntity.losses + userEntity.wins)) * 100
                String.format("%.2f", percentage) // Formats to 2 decimal places
            } else {
                0.00 // Default to 0.00 if no games have been played
            }
            binding.winPercentage.text = "Win Percentage : ${winPercentage} %"

            // Retrieve the image resource from the ViewModel
            val imageResId = menuInformationModel.getImageResource(userEntity.profilePic)

            if (imageResId != null) {
                binding.avatarView.setImageResource(imageResId) // Use the imageResId from the ViewModel
            } else {
                // Optionally set a default image if the resource ID is invalid
                binding.avatarView.setImageResource(R.drawable.avatar3) // Ensure this drawable exists
            }
        }
    }
}
