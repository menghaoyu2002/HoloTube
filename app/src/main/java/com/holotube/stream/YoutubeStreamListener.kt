package com.holotube.stream

import android.view.View
import com.holotube.databinding.FragmentStreamBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener

class YoutubeStreamListener(private val videoId: String) : AbstractYouTubePlayerListener() {

    override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.loadVideo(videoId, 0F)
    }
}

class StreamFullScreenListener(private val binding: FragmentStreamBinding) : YouTubePlayerFullScreenListener {

    override fun onYouTubePlayerEnterFullScreen() {
        binding.streamChat.visibility = View.GONE
    }

    override fun onYouTubePlayerExitFullScreen() {
        binding.streamChat.visibility = View.VISIBLE
    }
}