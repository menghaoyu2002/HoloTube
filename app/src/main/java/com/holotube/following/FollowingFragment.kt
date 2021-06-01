package com.holotube.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.MaterialToolbar
import com.holotube.R
import com.holotube.adapters.FollowingAdapter
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

        binding.followingList.adapter = FollowingAdapter(FollowingAdapter.OnClickListener { })
        binding.viewModel = viewModel

        return binding.root
    }

}