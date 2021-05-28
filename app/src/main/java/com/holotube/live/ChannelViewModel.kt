package com.holotube.live

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holotube.network.Channel
import com.holotube.network.ChannelList
import com.holotube.network.HoloApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChannelViewModel : ViewModel() {
    private val _channels = MutableLiveData<ChannelList>()

    val channels: LiveData<ChannelList>
        get() = _channels


    init {
        getChannels()
    }

    private fun getChannels() {
        viewModelScope.launch {
            try {
                _channels.value = HoloApi.retrofitService.getChannelList()
                Log.i("Response", "Success")
            } catch (e: Exception) {
                Log.i("Response", "Failed $e.message")
            }
        }
    }

}