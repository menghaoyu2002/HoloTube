package com.holotube.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.holotube.R
import com.holotube.adapters.LiveAdapter
import com.holotube.databinding.FragmentLiveBinding

class LiveFragment : Fragment() {

    private val viewModel by activityViewModels<ChannelViewModel>()
    private lateinit var binding: FragmentLiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getAllChannels()
            binding.root.invalidate()
            binding.swipeLayout.isRefreshing = false
        }

        binding.liveList.adapter = LiveAdapter(LiveAdapter.OnClickListener {
            viewModel.viewStream(it)
        })

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
    }
}