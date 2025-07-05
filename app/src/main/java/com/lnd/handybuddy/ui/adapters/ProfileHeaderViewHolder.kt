package com.lnd.handybuddy.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.lnd.handybuddy.databinding.ItemProfileHeaderBinding

class ProfileHeaderViewHolder(private val binding: ItemProfileHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.tvProfileName.text = "Lisset Noriega" // To do
        binding.tvEditProfile.setOnClickListener {
            // To do: action when click on Edit profile
        }
    }
}
