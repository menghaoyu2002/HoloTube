package com.holotube.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
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
        binding.upcomingList.adapter = UpcomingAdapter()

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