package com.holotube.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
                if (viewModel.isFollowed(it)) {
                    viewModel.unfollow(it)
                    Toast.makeText(context, "Unfollowed ${it.channelName}", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.follow(it)
                    Toast.makeText(context, "Followed ${it.channelName}", Toast.LENGTH_SHORT).show()
                }
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
    }
}