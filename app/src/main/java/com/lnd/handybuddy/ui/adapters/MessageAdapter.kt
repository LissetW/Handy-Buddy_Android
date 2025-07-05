package com.lnd.handybuddy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnd.handybuddy.data.local.entity.MessageEntity
import com.lnd.handybuddy.databinding.ItemMessageReceviedBinding
import com.lnd.handybuddy.databinding.ItemMessageSentBinding
import com.lnd.handybuddy.utils.formatTimestamp

class MessageAdapter(
    private val messages: List<MessageEntity>,
    private val technicianId: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sessionId == technicianId) {
            VIEW_TYPE_RECEIVED
        } else {
            VIEW_TYPE_SENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageReceviedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentMessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageEntity) {
            binding.tvMessage.text = message.content
            binding.tvTimestamp.text = formatTimestamp(message.timestamp)
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageReceviedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageEntity) {
            binding.tvMessage.text = message.content
            binding.tvTimestamp.text = formatTimestamp(message.timestamp)
        }
    }
}
