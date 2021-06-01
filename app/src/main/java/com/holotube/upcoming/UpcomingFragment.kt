package com.holotube.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.UpcomingAdapter
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
            if (viewModel.isFollowed(it)) {
                viewModel.unfollow(it)
                Toast.makeText(context, "Unfollowed", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.follow(it)
                Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show()
            }
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
}