package com.holotube.live

import android.util.Log
import androidx.lifecycle.*
import com.holotube.network.Channel
import com.holotube.network.ChannelList
import com.holotube.network.HoloApi
import kotlinx.coroutines.launch

enum class HoloApiStatus { LOADING, ERROR, DONE }

class ChannelViewModel : ViewModel() {
    private val _channels = MutableLiveData<ChannelList>()
    val channels: LiveData<ChannelList>
        get() = _channels

    private val _status = MutableLiveData<HoloApiStatus>()
    val status: LiveData<HoloApiStatus>
        get() = _status

    private val _navigateToStream = MutableLiveData<Channel>()
    val navigateToStream: LiveData<Channel>
        get() = _navigateToStream

    fun getAllChannels() {
        _status.value = HoloApiStatus.LOADING
        viewModelScope.launch {
            try {
                _channels.value = HoloApi.retrofitService.getChannelList()
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

}