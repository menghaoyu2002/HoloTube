package com.holotube.bindings


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.internal.bind.util.ISO8601Utils
import com.holotube.R
import com.holotube.adapters.FollowingAdapter
import com.holotube.adapters.LiveAdapter
import com.holotube.adapters.UpcomingAdapter
import com.holotube.database.ChannelEntity
import com.holotube.live.HoloApiStatus
import com.holotube.network.Channel
import com.holotube.util.CardViewUtils
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@BindingAdapter("listDataLive")
fun bindLiveRecyclerView(recyclerView: RecyclerView, data: List<Channel>?) {
    val adapter = recyclerView.adapter as LiveAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataUpcoming")
fun bindUpcomingRecyclerView(recyclerView: RecyclerView, data: List<Channel>?) {
    val adapter = recyclerView.adapter as UpcomingAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataFollowed")
fun bindFollowingRecyclerView(recyclerView: RecyclerView, data: List<ChannelEntity>?) {
    val adapter = recyclerView.adapter as FollowingAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .placeholder(ColorDrawable(Color.BLACK))
        .error(
            Glide.with(imgView.context)
                .load(imgUrl.subSequence(0, imgUrl.length - 17) as String + "hqdefault.jpg")
        )
        .into(imgView)
}

@BindingAdapter("startTime")
fun bindStartTime(textView: TextView, startTime: String?) {
    var timeDifference = if (startTime != null) CardViewUtils.getTimeDifference(startTime) else 0.0
    val reformattedTimeDifference: Int
    var unit: String

    if (abs(timeDifference) < 1) {
        unit = textView.context.getString(R.string.minutes)
        timeDifference *= 60
        reformattedTimeDifference = timeDifference.toInt()
    } else {
        reformattedTimeDifference = timeDifference.toInt()
        unit = textView.context.getString(R.string.hours)
    }

    if (abs(reformattedTimeDifference) != 1) {
        unit += "s"
    }

    val displayId: Int =
        if (timeDifference <= 0) R.string.stream_start_time_diff else R.string.upcoming_time_diff

    textView.text =
        textView.context.getString(
            displayId,
            abs(reformattedTimeDifference).toString(),
            unit
        )
}

@BindingAdapter("scheduledStart")
fun bindScheduledStart(textView: TextView, scheduledTime: String) {
    val date = ISO8601Utils.parse(scheduledTime, ParsePosition(0))
    val reformattedDate =
        SimpleDateFormat("yyyy-MM-dd 'at' HH:MM", Locale.getDefault()).format(date.time)
    textView.text = textView.context.getString(R.string.scheduledStart, reformattedDate)
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
    layout: LinearLayout,
    status: HoloApiStatus?
) {
    when (status) {
        HoloApiStatus.ERROR -> {
            layout.visibility = View.VISIBLE
        } else -> {
            layout.visibility = View.GONE
        }

    }
}