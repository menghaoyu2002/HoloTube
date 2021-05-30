package com.holotube.stream

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.material.appbar.MaterialToolbar
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

        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar).visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            }

        }

        val channel = requireArguments().getParcelable<Channel>("channel")!!
        binding.channel = channel
        binding.streamPlayer.initialize(YoutubeStreamListener(channel.videoKey))
        lifecycle.addObserver(binding.streamPlayer)

        initializeChat(binding)

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.show(WindowInsets.Type.statusBars())
        }
        requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar).visibility = View.VISIBLE
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeChat(binding: FragmentStreamBinding) {
        binding.streamChat.settings.javaScriptEnabled = true
        binding.streamChat.setInitialScale(180)
        binding.streamChat.settings.textZoom = 200

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
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}