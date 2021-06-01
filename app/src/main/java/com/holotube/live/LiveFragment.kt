package com.holotube.live

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = resources.getString(R.string.menuLiveLabel)

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels()
            binding.root.invalidate()
            binding.swipeLayout.isRefreshing = false
        }

        binding.liveList.adapter = LiveAdapter(
            LiveAdapter.OnClickListener { viewModel.viewStream(it) },
            LiveAdapter.OnLongClickListener {
                showFollowMenu(binding, viewModel, it)
            }
        )

        viewModel.navigateToStream.observe(viewLifecycleOwner, {
            if (null != it) {
                val bundle = Bundle()
                bundle.putParcelable("channel", it)
                this.findNavController()
                    .navigate(R.id.action_liveFragment_to_streamFragment, bundle)
                viewModel.finishedStream()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllChannels()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).invalidate()
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
}