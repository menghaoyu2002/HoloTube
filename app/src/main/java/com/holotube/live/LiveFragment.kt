package com.holotube.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.holotube.adapters.ChannelAdapter
import com.holotube.databinding.FragmentLiveBinding

class LiveFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()
    private lateinit var binding: FragmentLiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLiveBinding.inflate(inflater)


        binding.lifecycleOwner = this
        binding.liveList.adapter = ChannelAdapter()
        binding.viewModel = viewModel

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels()
            binding.liveList.invalidate()
            binding.swipeLayout.isRefreshing = false
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllChannels()
    }
}