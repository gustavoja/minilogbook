package com.gjapps.minilogbook.extensions

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun Date.toUTC(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.timeZone = TimeZone.getDefault()

    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.timeInMillis = calendar.timeInMillis

    val date = utcCalendar.time
    return date
}

fun Date.fromUTC(): Date {
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.time = this

    val localCalendar = Calendar.getInstance()
    localCalendar.timeInMillis = utcCalendar.timeInMillis

    val date = localCalendar.time
    return date
}