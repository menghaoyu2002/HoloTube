package com.holotube.live

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.holotube.adapters.LiveChannelAdapter
import com.holotube.databinding.FragmentLiveBinding

class LiveFragment : Fragment() {
    private val viewModel: ChannelViewModel by lazy {
        ViewModelProvider(this).get(ChannelViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLiveBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.channelList.adapter = LiveChannelAdapter()

        binding.viewModel = viewModel

        return binding.root
    }

}