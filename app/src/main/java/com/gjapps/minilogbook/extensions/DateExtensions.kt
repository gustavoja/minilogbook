package com.gjapps.minilogbook.extensions

import java.util.Date
import java.util.TimeZone

fun Date.toUTC(): Date {
    return Date(this.time - TimeZone.getDefault().getOffset(this.time))
}

fun Date.fromUTC(): Date {
    return Date(this.time + TimeZone.getDefault().getOffset(this.time))
}