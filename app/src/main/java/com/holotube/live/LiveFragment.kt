package com.holotube.live

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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.LiveAdapter
import com.holotube.database.FollowingDatabase
import com.holotube.databinding.FragmentLiveBinding


class LiveFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel> {
        ChannelViewModelFactory(
            FollowingDatabase.getInstance(requireContext().applicationContext).channelDao
        )
    }

    private lateinit var binding: FragmentLiveBinding

    private var channelFilter = ChannelFilters.A_TO_Z

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveBinding.inflate(inflater)
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.sortMenu.setOnClickListener { binding.sortMenu.visibility = View.GONE }
        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = resources.getString(R.string.menuLiveLabel)
        actionBar.menu[0].setOnMenuItemClickListener {
            binding.sortMenu.visibility = View.VISIBLE
            true
        }

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels(channelFilter, "Live")
            binding.swipeLayout.isRefreshing = false
        }

        binding.liveList.adapter = LiveAdapter(
            LiveAdapter.OnClickListener { viewModel.viewStream(it) },
            LiveAdapter.OnLongClickListener {
                showFollowMenu(binding, viewModel, it)
            }
        )

        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            onSortSelected(checkedId)
        }

        viewModel.navigateToStream.observe(viewLifecycleOwner, {
            if (null != it) {
                val bundle = Bundle()
                bundle.putParcelable("channel", it)
                binding.liveList.smoothScrollToPosition(0)
                this.findNavController()
                    .navigate(R.id.action_liveFragment_to_streamFragment, bundle)
                viewModel.finishedStream()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllChannels(channelFilter, "Live")
    }

    private fun showFollowMenu(
        binding: FragmentLiveBinding,
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
                viewModel.sort(ChannelFilters.A_TO_Z, "Live")
                channelFilter = ChannelFilters.A_TO_Z
            }
            R.id.Z_TO_A -> {
                viewModel.sort(ChannelFilters.Z_TO_A, "Live")
                channelFilter = ChannelFilters.Z_TO_A
            }
            R.id.VIEWCOUNT_LOW_TO_HIGH -> {
                viewModel.sort(
                    ChannelFilters.VIEWCOUNT_LOW_TO_HIGH,
                    "Live"
                )
                ChannelFilters.VIEWCOUNT_LOW_TO_HIGH
            }
            R.id.VIEWCOUNT_HIGH_TO_LOW -> {
                viewModel.sort(
                    ChannelFilters.VIEWCOUNT_HIGH_TO_LOW,
                    "Live"
                )
                channelFilter = ChannelFilters.VIEWCOUNT_HIGH_TO_LOW
            }
            R.id.START_TIME_LOW_TO_HIGH -> {
                viewModel.sort(
                    ChannelFilters.START_TIME_LOW_TO_HIGH,
                    "Live"
                )
                channelFilter = ChannelFilters.START_TIME_LOW_TO_HIGH
            }
            R.id.START_TIME_HIGH_TO_LOW -> {
                viewModel.sort(
                    ChannelFilters.START_TIME_HIGH_TO_LOW,
                    "Live"
                )
                channelFilter = ChannelFilters.START_TIME_HIGH_TO_LOW
            }
        }

        binding.viewModel = viewModel
        binding.liveList.smoothScrollToPosition(-10)
    }
}
