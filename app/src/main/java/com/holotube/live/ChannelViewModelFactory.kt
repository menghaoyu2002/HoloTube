package com.holotube.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.holotube.database.ChannelDao

class ChannelViewModelFactory(
    private val dataSource: ChannelDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelViewModel::class.java)) {
            return ChannelViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}