package com.holotube.stream

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.material.appbar.AppBarLayout
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
        val channel = requireArguments().getParcelable<Channel>("channel")!!
        binding.channel = channel
        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.title = channel.channelName
        actionBar.navigationIcon =
            AppCompatResources.getDrawable(requireContext(), R.mipmap.back_arrow_icon)
        actionBar.setNavigationOnClickListener { findNavController().popBackStack() }

        requireActivity().findViewById<AppBarLayout>(R.id.appBar).setExpanded(true)
        binding.streamPlayer.initialize(YoutubeStreamListener(channel.videoKey))
        lifecycle.addObserver(binding.streamPlayer)
        initializeStreamPlayerButtons(binding)
        initializeLandscape(binding)
        initializeChat(binding)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.show(WindowInsets.Type.statusBars())
        }
        val actionBar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        actionBar.visibility = View.VISIBLE
        actionBar.title = resources.getString(R.string.app_name)
        actionBar.navigationIcon = null
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

    private fun initializeLandscape(binding: FragmentStreamBinding) {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar).visibility =
                View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            }


        }
    }

    private fun initializeStreamPlayerButtons(binding: FragmentStreamBinding) {
        val size = resources.getDimension(R.dimen.chat_icon_size).toInt()
        val white = Color.argb(255, 255, 255, 255)

        val infoIcon = ImageView(context)
        infoIcon.setImageResource(R.mipmap.info_icon)
        infoIcon.setColorFilter(white)
        infoIcon.layoutParams = ViewGroup.LayoutParams(size, size)
        infoIcon.setOnClickListener {
            if (binding.infoCard.visibility == View.VISIBLE) {
                binding.infoCard.visibility = View.GONE
            } else {
                binding.infoCard.visibility = View.VISIBLE
            }
        }

        val chatIcon = ImageView(context)
        chatIcon.setImageResource(R.mipmap.chat_icon)
        chatIcon.setColorFilter(white)
        chatIcon.layoutParams = ViewGroup.LayoutParams(size, size)
        chatIcon.setOnClickListener {
            if (binding.streamChat.visibility == View.VISIBLE) {
                binding.streamChat.visibility = View.GONE
            } else {
                binding.streamChat.visibility = View.VISIBLE
            }
        }

        binding.streamPlayer.getPlayerUiController().addView(infoIcon)
        binding.streamPlayer.getPlayerUiController().addView(chatIcon)

    }
}