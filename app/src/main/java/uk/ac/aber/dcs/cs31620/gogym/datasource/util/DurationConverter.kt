package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import java.time.Duration

/**
 * Duration Converter
 */
object DurationConverter {
    @TypeConverter
    @JvmStatic
    fun durationToLong(duration: Duration): Long = duration.toMillis()

    @TypeConverter
    @JvmStatic
    fun longToDuration(millis: Long): Duration = Duration.ofMillis(millis)
}