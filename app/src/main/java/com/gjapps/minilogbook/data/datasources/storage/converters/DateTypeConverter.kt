package com.gjapps.minilogbook.data.datasources.storage.converters

import androidx.room.TypeConverter
import com.gjapps.minilogbook.extensions.fromUTC
import com.gjapps.minilogbook.extensions.toUTC
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it).fromUTC() }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.toUTC()?.time?.toLong()
    }
}