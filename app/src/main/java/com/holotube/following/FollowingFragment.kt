package com.holotube.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

        binding.followingList.adapter = FollowingAdapter(FollowingAdapter.OnLongClickListener {
            showUnfollowButton(binding, viewModel, it)
        })
        binding.viewModel = viewModel

        return binding.root
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