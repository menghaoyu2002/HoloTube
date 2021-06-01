package com.holotube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.holotube.database.ChannelEntity
import com.holotube.databinding.FollowingListItemBinding


class FollowingAdapter(private val onLongClickListener: OnLongClickListener) :
    ListAdapter<ChannelEntity, FollowingAdapter.ViewHolder>(ChannelDiffCallback) {

    class ViewHolder(
        private var binding:
        FollowingListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: ChannelEntity) {
            binding.channel = channel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FollowingListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onClick(item)
            true
        }
        holder.bind(item)
    }

    companion object ChannelDiffCallback : DiffUtil.ItemCallback<ChannelEntity>() {
        override fun areItemsTheSame(oldItem: ChannelEntity, newItem: ChannelEntity): Boolean {
            return oldItem.channelName == newItem.channelName
        }

        override fun areContentsTheSame(oldItem: ChannelEntity, newItem: ChannelEntity): Boolean {
            return oldItem == newItem
        }
    }

    class OnLongClickListener(val clickListener: (channel: ChannelEntity) -> Unit) {
        fun onClick(channel: ChannelEntity) = clickListener(channel)
    }
}

