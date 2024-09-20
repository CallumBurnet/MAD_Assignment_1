package com.example.mad_assignment_1

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.UserProfileBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class UserProfileAdapter(
    private var userEntities: List<UserEntity>, // List of UserEntity data
    private val onItemClick: (UserEntity) -> Unit, // Click listener for UserEntity
    private val menuInformationModel: MenuInformationModel // Pass the ViewModel
) : RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val binding = UserProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileViewHolder(binding, parent.context as LifecycleOwner)
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(userEntities[position])
    }

    override fun getItemCount(): Int = userEntities.size

    fun updateUserProfiles(newUserEntities: List<UserEntity>) {
        userEntities = newUserEntities
        notifyDataSetChanged()
    }

    inner class UserProfileViewHolder(private val binding: UserProfileBinding,
    private val lifecycle: LifecycleOwner) :
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
            var gamesPlayed = userEntity.draws + userEntity.wins + userEntity.losses;
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
            Log.d("AVT", "Image resource ${userEntity.profilePic} to ${imageResId}")

            if (imageResId != null) {
                binding.avatarView.setImageResource(imageResId) // Use the imageResId from the ViewModel
            } else {
                // Optionally set a default image if the resource ID is invalid
                binding.avatarView.setImageResource(R.drawable.avatar3) // Ensure this drawable exists
            }

            menuInformationModel.activePrimaryUser.observe(lifecycle) { primaryUser ->
                if  (primaryUser.userID == userEntity.userID) {
                    binding.userProfileInfo.setBackgroundColor(Color.parseColor("#a6a6ff"))
                } else if (menuInformationModel.activeSecondaryUser.value?.userID != userEntity.userID) {
                    binding.userProfileInfo.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
            menuInformationModel.activeSecondaryUser.observe(lifecycle) { seondaryUser ->
                if  (seondaryUser.userID == userEntity.userID) {
                    binding.userProfileInfo.setBackgroundColor(Color.parseColor("#ffa6a6"))
                } else if (menuInformationModel.activePrimaryUser.value?.userID != userEntity.userID) {
                    binding.userProfileInfo.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
        }
    }
}
