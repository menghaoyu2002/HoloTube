package com.holotube.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "following_table")
data class ChannelEntity constructor(
    @PrimaryKey
    val channelName: String,

    @ColumnInfo(name = "stream_title")
    val streamTitle: String,

    @ColumnInfo(name = "scheduled_start")
    val scheduledStart: String,

    @ColumnInfo(name = "start_time")
    val startTime: String?,

    @ColumnInfo(name = "view_count")
    val viewCount: String?,

    @ColumnInfo(name = "video_key")
    val videoKey: String,

    @ColumnInfo(name = "profile_picture")
    val profilePictureUrl: String,

    @ColumnInfo(name = "stream_thumbnail")
    val streamThumbnail: String,

    @ColumnInfo(name = "stream_chat")
    val streamChat: String
)
