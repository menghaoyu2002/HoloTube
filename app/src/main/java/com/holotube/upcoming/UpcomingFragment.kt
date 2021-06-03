package com.holotube.upcoming

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.UpcomingAdapter
import com.holotube.databinding.FragmentUpcomingBinding
import com.holotube.live.ChannelFilters
import com.holotube.live.ChannelViewModel


class UpcomingFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()
    private lateinit var binding: FragmentUpcomingBinding

    private var channelFilter = ChannelFilters.START_TIME_LOW_TO_HIGH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater)

        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = resources.getString(R.string.menuUpcomingLabel)
        actionBar.setOnClickListener { binding.upcomingList.smoothScrollToPosition(0) }
        requireActivity().findViewById<AppBarLayout>(R.id.appBar).setExpanded(true)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.upcomingList.adapter = UpcomingAdapter(UpcomingAdapter.OnLongClickListener {
            showFollowMenu(binding, viewModel, it)
        })

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels(channelFilter, "Upcoming")
            binding.swipeLayout.isRefreshing = false
        }

        binding.sortMenu.setOnClickListener { binding.sortMenu.visibility = View.GONE }
        actionBar.menu[0].setOnMenuItemClickListener {
            binding.sortMenu.visibility = View.VISIBLE
            true
        }

        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            onSortSelected(checkedId)
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

    private fun onSortSelected(checkedId: Int) {
        when (checkedId) {
            R.id.A_TO_Z -> {
                channelFilter = ChannelFilters.A_TO_Z
            }
            R.id.Z_TO_A -> {
                channelFilter = ChannelFilters.Z_TO_A
            }
            R.id.START_TIME_LOW_TO_HIGH -> {

                channelFilter = ChannelFilters.START_TIME_LOW_TO_HIGH
            }
            R.id.START_TIME_HIGH_TO_LOW -> {
                channelFilter = ChannelFilters.START_TIME_HIGH_TO_LOW
            }
        }
        viewModel.getAllChannels(channelFilter, "Upcoming")
    }
}