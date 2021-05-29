package com.holotube.bindings


import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.internal.bind.util.ISO8601Utils
import com.holotube.R
import com.holotube.adapters.ChannelAdapter
import com.holotube.adapters.UpcomingAdapter
import com.holotube.live.HoloApiStatus
import com.holotube.network.Channel
import com.holotube.util.CardViewUtils
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

@BindingAdapter("listDataLive")
fun bindLiveRecyclerView(recyclerView: RecyclerView, data: List<Channel>?) {
    val adapter = recyclerView.adapter as ChannelAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataUpcoming")
fun bindUpcomingRecyclerView(recyclerView: RecyclerView, data: List<Channel>?) {
    val adapter = recyclerView.adapter as UpcomingAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .error(
            Glide.with(imgView.context)
                .load(imgUrl.subSequence(0, imgUrl.length - 17) as String + "hqdefault.jpg")
        )
        .into(imgView)
}

@BindingAdapter("startTime")
fun bindStartTime(textView: TextView, startTime: String) {
    var timeDifference = CardViewUtils.getTimeDifference(startTime)
    var unit: String

    if (abs(timeDifference) < 1) {
        unit = textView.context.getString(R.string.minutes)
        timeDifference *= 60
    } else {
        unit = textView.context.getString(R.string.hours)
    }

    if (abs(timeDifference) != 1.toLong()) {
        unit += "s"
    }

    val displayId: Int = if (timeDifference < 1) {
        R.string.stream_start_time_diff
    } else {
        R.string.upcoming_time_diff
    }

    textView.text =
        textView.context.getString(
            displayId,
            abs(timeDifference).toString(),
            unit
        )
}

@BindingAdapter("scheduledStart")
fun bindScheduledStart(textView: TextView, scheduledTime: String) {
    val date = ISO8601Utils.parse(scheduledTime, ParsePosition(0))
    if (CardViewUtils.getTimeDifference(scheduledTime) > 24) {
        val reformattedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
        textView.text = textView.context.getString(R.string.scheduledStart, reformattedDate)
    } else {
        bindStartTime(textView, scheduledTime)
    }
}

@BindingAdapter("viewCount")
fun bindViewCount(textView: TextView, viewCount: String?) {
    if (viewCount != null) {
        val reformattedViewCount = if (viewCount.toLong() > 1000) {
            (viewCount.toLong() / 1000).toString() + "K"
        } else {
            viewCount
        }
        textView.text = textView.context.getString(R.string.viewCount, reformattedViewCount)
        textView.visibility = View.VISIBLE
    } else {
        textView.text = ""
        textView.visibility = View.INVISIBLE
    }
}

@BindingAdapter("HoloApiStatus")
fun bindStatus(
    textView: TextView,
    status: HoloApiStatus?
) {
    when (status) {
        HoloApiStatus.ERROR -> {
            textView.visibility = View.VISIBLE
            textView.text = "Error: No Connection"
        }
        HoloApiStatus.LOADING -> {
            textView.visibility = View.VISIBLE
            textView.text = "Loading..."
        }
        HoloApiStatus.DONE -> {
            textView.visibility = View.GONE
        }

    }
}