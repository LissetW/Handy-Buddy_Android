package com.lnd.handybuddy.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lnd.handybuddy.R
import com.lnd.handybuddy.data.remote.model.TechnicianDto
import com.lnd.handybuddy.databinding.TechnicianRowElementBinding

class TechnicianViewHolder(
    private val binding: TechnicianRowElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(technician: TechnicianDto) {
        val name = technician.name ?: "Unknown"
        val lastName = technician.lastName ?: ""
        binding.tvName.text = binding.root.context.getString(
            R.string.full_name_technician,
            name,
            lastName
        )
        binding.tvRating.text = technician.ratingAverage.toString()
        binding.tvSpecialities.text = technician.typeOfWork?.joinToString(", ") ?: ""
        binding.rbRating.rating = technician.ratingAverage?.toFloat() ?: 0f
        Glide.with(binding.root.context)
            .load(technician.image)
            .circleCrop()
            .into(binding.ivProfile)
    }
}