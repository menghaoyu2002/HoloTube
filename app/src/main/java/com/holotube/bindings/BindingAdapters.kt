package com.holotube.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.holotube.adapters.LiveChannelAdapter
import com.holotube.network.Channel

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Channel>?) {
    val adapter = recyclerView.adapter as LiveChannelAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .into(imgView)
}

