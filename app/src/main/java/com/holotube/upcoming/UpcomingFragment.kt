package com.holotube.upcoming

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.UpcomingAdapter
import com.holotube.databinding.FragmentLiveBinding
import com.holotube.databinding.FragmentUpcomingBinding
import com.holotube.live.ChannelViewModel


class UpcomingFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUpcomingBinding.inflate(inflater)

        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = resources.getString(R.string.menuUpcomingLabel)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.upcomingList.adapter = UpcomingAdapter(UpcomingAdapter.OnLongClickListener {
            showFollowMenu(binding, viewModel, it)
        })

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels()
            binding.swipeLayout.isRefreshing = false
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllChannels()
    }

    private fun showFollowMenu(
        binding: FragmentUpcomingBinding,
        viewModel: ChannelViewModel,
        channel: com.holotube.network.Channel
    ) {
        binding.channelProfile.visibility = View.VISIBLE
        binding.channelProfile.setOnClickListener {
            binding.channelProfile.visibility = View.GONE
        }
        Glide.with(binding.channelProfilePicture.context)
            .load(channel.profilePictureUrl)
            .placeholder(ColorDrawable(Color.BLACK))
            .into(binding.channelProfilePicture)

        if (viewModel.isFollowed(channel)) {
            binding.followUnfollowButton.text =
                getString(R.string.unfollow_button, channel.channelName)
            binding.followUnfollowButton.setOnClickListener {
                viewModel.unfollow(channel.channelName)
                binding.channelProfile.visibility = View.GONE
                Toast.makeText(context, "Unfollowed ${channel.channelName}", Toast.LENGTH_SHORT)
                    .show()

            }
        } else {
            binding.followUnfollowButton.text =
                getString(R.string.follow_button, channel.channelName)
            binding.followUnfollowButton.setOnClickListener {
                viewModel.follow(channel)
                binding.channelProfile.visibility = View.GONE
                Toast.makeText(context, "Followed ${channel.channelName}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}