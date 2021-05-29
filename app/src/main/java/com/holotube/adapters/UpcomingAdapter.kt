package com.holotube.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.holotube.databinding.UpcomingListItemBinding
import com.holotube.network.Channel

class UpcomingAdapter : ListAdapter<Channel, UpcomingAdapter.ViewHolder>(ChannelDiffCallback) {
    class ViewHolder(
        private var binding:
        UpcomingListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: Channel) {
            binding.channel = channel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UpcomingListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.videoKey == newItem.videoKey
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }
}