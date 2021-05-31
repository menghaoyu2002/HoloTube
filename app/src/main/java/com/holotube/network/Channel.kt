package com.holotube.network

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

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
) : Parcelable {
    val channelName: String = channelJson["name"] as String
    val profilePictureUrl: String = channelJson["photo"] as String
    val streamThumbnail: String = "https://img.youtube.com/vi/$videoKey/maxresdefault.jpg"
    val streamChat: String = "https://www.youtube.com/live_chat?is_popout=1&v=$videoKey"

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        mapOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoKey)
        parcel.writeString(streamTitle)
        parcel.writeString(scheduledStart)
        parcel.writeString(startTime)
        parcel.writeString(viewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Channel> {
        override fun createFromParcel(parcel: Parcel): Channel {
            return Channel(parcel)
        }

        override fun newArray(size: Int): Array<Channel?> {
            return arrayOfNulls(size)
        }
    }
}
