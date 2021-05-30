package com.holotube.stream

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class YoutubeStreamListener(private val videoId: String) : AbstractYouTubePlayerListener() {

    override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.loadVideo(videoId, 0F)
    }
}