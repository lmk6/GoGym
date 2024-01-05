package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object DurationConverter {
    @TypeConverter
    @JvmStatic
    fun durationToLong(duration: Duration): Long =
        duration.toLong(DurationUnit.MILLISECONDS)

    @TypeConverter
    @JvmStatic
    fun longToDuration(millis: Long): Duration =
        millis.toDuration(DurationUnit.MILLISECONDS)
}