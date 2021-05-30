package com.holotube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.holotube.databinding.LiveListItemBinding
import com.holotube.network.Channel

class LiveAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Channel, LiveAdapter.ViewHolder>(ChannelDiffCallback) {

    class ViewHolder(
        private var binding:
        LiveListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: Channel) {
            binding.channel = channel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LiveListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    companion object ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.videoKey == newItem.videoKey
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (channel: Channel) -> Unit) {
        fun onClick(channel: Channel) = clickListener(channel)
    }
}

