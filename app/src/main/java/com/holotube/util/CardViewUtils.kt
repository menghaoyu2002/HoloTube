package com.holotube.util

import com.google.gson.internal.bind.util.ISO8601Utils
import java.text.ParsePosition
import java.util.*

object CardViewUtils {
    fun getTimeDifference(inputDate: String): Double {
        val startTime = ISO8601Utils.parse(inputDate, ParsePosition(0))
        val currentTime = Calendar.getInstance().time
        return (startTime.time.toDouble() - currentTime.time.toDouble()) / (1000 * 60 * 60)
    }
}