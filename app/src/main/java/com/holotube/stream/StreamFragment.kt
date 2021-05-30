package com.holotube.stream

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holotube.R
import com.holotube.databinding.FragmentStreamBinding
import com.holotube.network.Channel

class StreamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStreamBinding.inflate(inflater)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE

        val channel = requireArguments().getParcelable<Channel>("channel")!!
        binding.channel = channel

        binding.streamPlayer.initialize(YoutubeStreamListener(channel.videoKey))
        lifecycle.addObserver(binding.streamPlayer)

        initializeChat(binding)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeChat(binding: FragmentStreamBinding) {
        binding.streamChat.settings.javaScriptEnabled = true
        binding.streamChat.setInitialScale(180)
        binding.streamChat.settings.textZoom = 160
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK) && isDarkThemeOn()) {
            when (isDarkThemeOn()) {
                true -> WebSettingsCompat.setForceDark(
                    binding.streamChat.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
                false -> WebSettingsCompat.setForceDark(
                    binding.streamChat.settings,
                    WebSettingsCompat.FORCE_DARK_OFF
                )
            }
        }

        binding.streamChat.loadUrl(binding.channel!!.streamChat)
    }

    private fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }

}