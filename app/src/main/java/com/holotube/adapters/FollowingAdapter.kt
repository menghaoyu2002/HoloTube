package com.holotube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.holotube.database.ChannelEntity
import com.holotube.databinding.FollowingListItemBinding
import com.holotube.live.ChannelViewModel

private const val LIVE_CHANNEL = 0
private const val OFFLINE_CHANNEL = 1

class FollowingAdapter(
    private val onClickListener: OnClickListener,
    private val onLongClickListener: OnLongClickListener,
    private val viewModel: ChannelViewModel
) :
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
        val binding =
            FollowingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        when (viewType) {
            0 -> binding.liveIcon.visibility = View.VISIBLE
            else -> binding.liveIcon.visibility = View.GONE
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onClick(item)
            true
        }
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewModel.getLiveChannelByName(getItem(position).channelName)) {
            null -> OFFLINE_CHANNEL
            else -> LIVE_CHANNEL
        }
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

    class OnClickListener(val clickListener: (channel: ChannelEntity) -> Unit) {
        fun onClick(channel: ChannelEntity) = clickListener(channel)
    }
}

