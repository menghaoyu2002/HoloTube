package com.holotube.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.FollowingAdapter
import com.holotube.database.ChannelEntity
import com.holotube.databinding.FragmentFollowingBinding
import com.holotube.live.ChannelViewModel


class FollowingFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFollowingBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = resources.getString(R.string.following)
        actionBar.setOnClickListener { binding.followingList.smoothScrollToPosition(0) }
        actionBar.menu[0].isVisible = false

        binding.followingList.adapter = FollowingAdapter(
            FollowingAdapter.OnClickListener {
                when (val channel = viewModel.getLiveChannelByName(it.channelName)) {
                    null -> Toast.makeText(
                        context,
                        "Sorry, this stream is offline",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> viewModel.viewStream(channel)
                }
            },
            FollowingAdapter.OnLongClickListener {
                showUnfollowButton(
                    binding,
                    viewModel,
                    it
                )
            },
            viewModel
        )
        binding.viewModel = viewModel

        viewModel.navigateToStream.observe(viewLifecycleOwner, {
            if (null != it) {
                val bundle = Bundle()
                bundle.putParcelable("channel", it)
                this.findNavController()
                    .navigate(R.id.action_followingFragment_to_streamFragment, bundle)
                viewModel.finishedStream()
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar).menu[0].isVisible = true
    }

    private fun showUnfollowButton(
        binding: FragmentFollowingBinding,
        viewModel: ChannelViewModel,
        channel: ChannelEntity
    ) {
        binding.unfollowView.visibility = View.VISIBLE
        binding.unfollowView.setOnClickListener { binding.unfollowView.visibility = View.GONE }
        binding.unfollowButton.setOnClickListener {
            viewModel.unfollow(channel.channelName)
            binding.unfollowView.visibility = View.GONE
            Toast.makeText(context, "Unfollowed ${channel.channelName}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}