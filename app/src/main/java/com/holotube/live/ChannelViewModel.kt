package com.holotube.live

import android.util.Log
import androidx.lifecycle.*
import com.holotube.network.Channel
import com.holotube.network.ChannelList
import com.holotube.network.HoloApi
import kotlinx.coroutines.launch

enum class HoloApiStatus {LOADING, DONE, ERROR}

class ChannelViewModel : ViewModel() {
    private val _channels = MutableLiveData<ChannelList>()
    val channels: LiveData<ChannelList>
        get() = _channels

    private val _navigateToStream = MutableLiveData<Channel>()
    val navigateToStream: LiveData<Channel>
        get() = _navigateToStream

    private val _status = MutableLiveData<HoloApiStatus>()
    val status: LiveData<HoloApiStatus>
        get() = _status

    fun getAllChannels() {
        _status.value = HoloApiStatus.LOADING
        viewModelScope.launch {
            try {
                _channels.value = HoloApi.retrofitService.getChannelList()
                initializeChannelList()
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

    private fun initializeChannelList() {
        _channels.value!!.live = _channels.value!!.live.sortedByDescending { it.startTime }
        _channels.value!!.upcoming = _channels.value!!.upcoming.sortedBy { it.scheduledStart }
    }
}