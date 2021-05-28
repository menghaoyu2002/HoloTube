package com.holotube.network

import com.squareup.moshi.Json
import java.util.*

data class ChannelList(
    val live: List<Channel>,
    val upcoming: List<Channel>
)

data class Channel(
    @Json(name = "yt_video_key") val videoKey: String,
    @Json(name = "title") val streamTitle: String,
    @Json(name = "live_schedule") val scheduledStart: String,
    @Json(name = "live_start") val startTime: String?,
    @Json(name = "live_viewers") val viewCount: String?,
    @Json(name = "channel") private val channelJson: Map<String, String>,
) {
    val channelName: String = channelJson["name"] as String
    val profilePictureUrl: String = channelJson["photo"] as String
    val streamLink: String = "https://www.youtube.com/watch?v=$videoKey"
    val streamThumbnail: String = "https://img.youtube.com/vi/$videoKey/maxresdefault.jpg"
}
