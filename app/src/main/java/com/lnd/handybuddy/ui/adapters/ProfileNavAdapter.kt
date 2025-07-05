package com.lnd.handybuddy.ui.adapters

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnd.handybuddy.databinding.ItemProfileHeaderBinding
import com.lnd.handybuddy.databinding.ItemSectionHeaderBinding
import com.lnd.handybuddy.databinding.ProfileItemNavButtonBinding
import com.lnd.handybuddy.ui.fragments.NavItem

class ProfileNavAdapter (
    private val items: List<NavItem>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SECTION = 0
        private const val TYPE_OPTION = 1
        private const val TYPE_PROFILE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is NavItem.ProfileHeader -> TYPE_PROFILE
            is NavItem.SectionHeader -> TYPE_SECTION
            is NavItem.Option -> TYPE_OPTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SECTION -> {
                val binding = ItemSectionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SectionViewHolder(binding)
            }
            TYPE_OPTION -> {
                val binding = ProfileItemNavButtonBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                OptionViewHolder(binding, onClick)
            }
            TYPE_PROFILE -> {
                val binding = ItemProfileHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ProfileHeaderViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is NavItem.ProfileHeader -> (holder as ProfileHeaderViewHolder).bind()
            is NavItem.SectionHeader -> (holder as SectionViewHolder).bind(item)
            is NavItem.Option -> (holder as OptionViewHolder).bind(item)
            else -> {}
        }
    }

    class SectionViewHolder(private val binding: ItemSectionHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NavItem.SectionHeader) {
            binding.sectionTitle.text = item.title
        }
    }

    class OptionViewHolder(
        private val binding: ProfileItemNavButtonBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NavItem.Option) {
            binding.tvOption.text = item.title

            if (item.title.equals("Log Out", ignoreCase = true)) {
                binding.tvOption.setTextColor(Color.RED)
                binding.tvOption.textAlignment = View.TEXT_ALIGNMENT_CENTER
                binding.tvOption.gravity = Gravity.CENTER
                binding.ivArrow.visibility = View.GONE
            } else {
                binding.tvOption.setTextColor(Color.BLACK)
                binding.tvOption.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                binding.tvOption.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                binding.ivArrow.visibility = View.VISIBLE
            }

            binding.root.setOnClickListener { onClick(item.title) }
        }
    }
}