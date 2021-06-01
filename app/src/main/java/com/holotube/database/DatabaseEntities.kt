package com.holotube.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "following_table")
data class ChannelEntity constructor(
    @PrimaryKey
    val channelName: String,

    @ColumnInfo(name = "profile_picture")
    val profilePictureUrl: String,

)
