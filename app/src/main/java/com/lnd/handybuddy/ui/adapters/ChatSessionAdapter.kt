package com.lnd.handybuddy.ui.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lnd.handybuddy.R
import com.lnd.handybuddy.data.local.entity.ChatSessionEntity
import com.lnd.handybuddy.databinding.ItemChatBinding
import com.lnd.handybuddy.utils.formatTimestamp

class ChatSessionAdapter(
    private var sessions: List<ChatSessionEntity>,
    private val onClick: (ChatSessionEntity) -> Unit
) : RecyclerView.Adapter<ChatSessionAdapter.ChatSessionViewHolder>() {

    inner class ChatSessionViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(session: ChatSessionEntity) {
            binding.tvMessage.text = session.lastMessage
            binding.tvTimestamp.text = formatTimestamp(session.timestamp)

            val technicianName = listOfNotNull(session.technicianName, session.technicianLastName)
                .joinToString(" ")
                .ifEmpty { binding.root.context.getString(R.string.technician_default_name) }

            binding.tvTechnicianName.text = technicianName

            Glide.with(binding.root.context)
                .load(session.technicianImage)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.ivTechnicianPhoto)

            binding.root.setOnClickListener {
                onClick(session)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatSessionViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatSessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatSessionViewHolder, position: Int) {
        holder.bind(sessions[position])
    }

    override fun getItemCount(): Int = sessions.size

    fun updateData(newSessions: List<ChatSessionEntity>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}
