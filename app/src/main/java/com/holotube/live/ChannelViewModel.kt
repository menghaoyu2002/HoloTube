package com.holotube.live

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.holotube.database.ChannelDao
import com.holotube.database.ChannelEntity
import com.holotube.network.Channel
import com.holotube.network.ChannelList
import com.holotube.network.HoloApi
import kotlinx.coroutines.launch

enum class HoloApiStatus { LOADING, DONE, ERROR }

enum class ChannelFilters {
    A_TO_Z,
    Z_TO_A,
    VIEWCOUNT_HIGH_TO_LOW,
    VIEWCOUNT_LOW_TO_HIGH,
    START_TIME_LOW_TO_HIGH,
    START_TIME_HIGH_TO_LOW
}

class ChannelViewModel(database: ChannelDao) : ViewModel() {
    private val _channels = MutableLiveData<ChannelList>()
    val channels: LiveData<ChannelList>
        get() = _channels

    private val _navigateToStream = MutableLiveData<Channel>()
    val navigateToStream: LiveData<Channel>
        get() = _navigateToStream

    private val _status = MutableLiveData<HoloApiStatus>()
    val status: LiveData<HoloApiStatus>
        get() = _status

    private val dataSource = database

    private val _followed = database.getAllFollowing()
    val followed: LiveData<List<ChannelEntity>>
        get() = _followed

    fun getAllChannels(channelFilter: ChannelFilters? = null, channelStatus: String? = null) {
        _status.value = HoloApiStatus.LOADING
        viewModelScope.launch {
            try {
                _channels.value = HoloApi.retrofitService.getChannelList()
                if (channelFilter != null && channelStatus != null) {
                    sort(channelFilter, channelStatus)
                } else {
                    sort(ChannelFilters.A_TO_Z, "Live")
                    sort(ChannelFilters.START_TIME_LOW_TO_HIGH, "Upcoming")
                }
                _status.value = HoloApiStatus.DONE
            } catch (e: Exception) {
                Log.e("Response", "Failed $e.message")
                _status.value = HoloApiStatus.ERROR
            }
        }
    }

    fun viewStream(channel: Channel) {
        _navigateToStream.value = channel
    }

    fun finishedStream() {
        _navigateToStream.value = null
    }

    fun follow(channel: Channel) {
        dataSource.follow(ChannelEntity(channel.channelName, channel.profilePictureUrl))
    }

    fun unfollow(channelName: String) {
        dataSource.unfollow(channelName)
    }

    fun isFollowed(channel: Channel): Boolean {
        return dataSource.get(channel.channelName) != null
    }

    fun getLiveChannelByName(channelName: String): Channel? {
        return channels.value?.live?.find { it.channelName == channelName }
    }

    fun sort(channelFilter: ChannelFilters, channelStatus: String) {
        if (_channels.value == null) return
        if (channelStatus == "Live") {
            when (channelFilter) {
                ChannelFilters.A_TO_Z -> {
                    _channels.value!!.live =
                        _channels.value!!.live.sortedBy { it.channelName }
                }

                ChannelFilters.Z_TO_A -> _channels.value!!.live =
                    _channels.value!!.live.sortedByDescending { it.channelName }

                ChannelFilters.VIEWCOUNT_HIGH_TO_LOW -> _channels.value!!.live =
                    _channels.value!!.live.sortedByDescending { it.viewCount!!.toLong() }

                ChannelFilters.VIEWCOUNT_LOW_TO_HIGH -> _channels.value!!.live =
                    _channels.value!!.live.sortedBy { it.viewCount!!.toLong() }

                ChannelFilters.START_TIME_LOW_TO_HIGH -> _channels.value!!.live =
                    _channels.value!!.live.sortedByDescending { it.startTime }

                ChannelFilters.START_TIME_HIGH_TO_LOW -> _channels.value!!.live =
                    _channels.value!!.live.sortedBy { it.startTime }
            }
        } else {
            when (channelFilter) {
                ChannelFilters.A_TO_Z -> _channels.value!!.upcoming =
                    _channels.value!!.upcoming.sortedBy { it.channelName }

                ChannelFilters.Z_TO_A -> _channels.value!!.upcoming =
                    _channels.value!!.upcoming.sortedByDescending { it.channelName }

                ChannelFilters.START_TIME_LOW_TO_HIGH -> _channels.value!!.upcoming =
                    _channels.value!!.upcoming.sortedBy { it.scheduledStart }

                ChannelFilters.START_TIME_HIGH_TO_LOW -> _channels.value!!.upcoming =
                    _channels.value!!.upcoming.sortedByDescending { it.scheduledStart }
                else -> return
            }
        }
    }
}