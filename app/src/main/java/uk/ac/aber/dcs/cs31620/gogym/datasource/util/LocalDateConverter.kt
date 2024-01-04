@file:Suppress("Since15")

package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object LocalDateConverter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    @JvmStatic
    fun toLocalDate(date: String): LocalDate =
        date.let { LocalDate.parse(it, formatter) }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(localDate: LocalDate): String =
        localDate.format(formatter)
}