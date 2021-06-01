package com.holotube.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.holotube.network.Channel

@Dao
interface ChannelDao {
    @Insert
    fun follow(channel: ChannelEntity)

    @Update
    fun updateAll(channel: ChannelEntity)

    @Query("SELECT * FROM following_table WHERE channelName = :channelName")
    fun get(channelName: String): ChannelEntity?

    @Query("DELETE FROM following_table WHERE channelName = :channelName")
    fun unfollow(channelName: String)

    @Query("SELECT * FROM following_table ORDER BY channelName")
    fun getAllFollowing(): List<ChannelEntity>
}