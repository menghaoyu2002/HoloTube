package com.holotube.live

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.holotube.adapters.ChannelAdapter
import com.holotube.databinding.FragmentLiveBinding

class LiveFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLiveBinding.inflate(inflater)

        viewModel.getAllChannels()
        binding.lifecycleOwner = this
        binding.liveList.adapter = ChannelAdapter()
        binding.viewModel = viewModel

        return binding.root
    }

}