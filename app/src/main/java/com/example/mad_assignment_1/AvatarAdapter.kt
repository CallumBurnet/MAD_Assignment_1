package com.example.mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
//Class for the management of avatars
class AvatarAdapter(
    private val avatars : List<Int>,
    private val onClick: (Int) -> Unit
): RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>(){
    class AvatarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val avatarImageView: ImageView = itemView.findViewById(R.id.avaterItemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.avatar_item, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarResId = avatars[position]
        holder.avatarImageView.setImageResource(avatarResId)

        holder.itemView.setOnClickListener{
            onClick(position)
        }
    }
    override fun getItemCount(): Int = avatars.size

}


