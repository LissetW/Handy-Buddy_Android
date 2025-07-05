package com.lnd.handybuddy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnd.handybuddy.data.remote.model.TechnicianDto
import com.lnd.handybuddy.databinding.TechnicianRowElementBinding

class TechniciansAdapter(
    private var technicianList: List<TechnicianDto>,
    private val onTechnicianClick: (TechnicianDto) -> Unit
) : RecyclerView.Adapter<TechnicianViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicianViewHolder {
        val binding = TechnicianRowElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechnicianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechnicianViewHolder, position: Int) {
        val technician = technicianList[position]
        holder.bind(technician)

        holder.itemView.setOnClickListener {
            onTechnicianClick(technician)
        }
    }

    override fun getItemCount(): Int = technicianList.size

    fun updateData(newList: List<TechnicianDto>) {
        technicianList = newList
        notifyDataSetChanged()
    }
}
